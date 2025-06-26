#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <jni.h>
#include <string>
#include <vector>
// ncnn
#include "net.h"
#include "ts.id.h"
#include <sys/time.h>
#include <unistd.h>
#define LOG_TAG "MobileNetJNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static ncnn::UnlockedPoolAllocator g_blob_pool_allocator;
static ncnn::PoolAllocator g_workspace_pool_allocator;

static ncnn::Mat ncnn_param;
static ncnn::Mat ncnn_bin;
static ncnn::Net mobilenet;
static AAssetManager* g_asset_manager = nullptr;

extern "C" {

JNIEXPORT jboolean
Java_com_example_whichbin_MobileNet_init(JNIEnv* env, jobject thiz, jobject asset_manager) {
    g_asset_manager = AAssetManager_fromJava(env, asset_manager);
    if (!g_asset_manager) {
        LOGE("Failed to get AssetManager");
        return JNI_FALSE;
    }

    // 加载模型文件
    AAsset* param_asset = AAssetManager_open(g_asset_manager, "best.param", AASSET_MODE_BUFFER);
    AAsset* bin_asset = AAssetManager_open(g_asset_manager, "best.bin", AASSET_MODE_BUFFER);
    if (!param_asset || !bin_asset) {
        LOGE("Failed to open model files");
        return JNI_FALSE;
    }

    // 读取文件内容
    const void* param_data = AAsset_getBuffer(param_asset);
    const void* bin_data = AAsset_getBuffer(bin_asset);
    off_t param_size = AAsset_getLength(param_asset);
    off_t bin_size = AAsset_getLength(bin_asset);


    // 加载模型到 ncnn
    int ret = mobilenet.load_param_mem(reinterpret_cast<const char*>(param_data));
    if (ret != 0) {
        LOGE("Failed to load param file");
        AAsset_close(param_asset);
        AAsset_close(bin_asset);
        return JNI_FALSE;
    }

    ret = mobilenet.load_model(reinterpret_cast<const unsigned char*>(bin_data));
    AAsset_close(param_asset);
    AAsset_close(bin_asset);


    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator;
    opt.workspace_allocator = &g_workspace_pool_allocator;

    if (ret != 0) {
        LOGE("Failed to load bin file");
        return JNI_FALSE;
    }

    LOGD("Model loaded successfully");
    return JNI_TRUE;
}

// public native String Detect(Bitmap bitmap);
JNIEXPORT jfloatArray Java_com_example_whichbin_MobileNet_detect(JNIEnv* env, jobject thiz, jobject bitmap)
{
    // ncnn from bitmap
    ncnn::Mat in;
    {
        AndroidBitmapInfo info;
        AndroidBitmap_getInfo(env, bitmap, &info);
        int width = info.width;
        int height = info.height;
        if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
            return NULL;

        void* indata;
        AndroidBitmap_lockPixels(env, bitmap, &indata);
        // 把像素转换成data，并指定通道顺序
        in = ncnn::Mat::from_pixels((const unsigned char*)indata, ncnn::Mat::PIXEL_RGBA2BGR, width, height);

        AndroidBitmap_unlockPixels(env, bitmap);
    }

    // ncnn_net
    std::vector<float> cls_scores;
    {
        // 减去均值和乘上比例
        const float mean_vals[3] = {103.94f, 116.78f, 123.68f};
        const float scale[3] = {0.017f, 0.017f, 0.017f};

        in.substract_mean_normalize(mean_vals, scale);

        ncnn::Extractor ex = mobilenet.create_extractor();
        // 如果时不加密是使用ex.input("data", in);
        ex.input(ts_param_id::BLOB_input, in);

        ncnn::Mat out;
        // 如果时不加密是使用ex.extract("prob", out);
        ex.extract(ts_param_id::BLOB_probs, out);

        int output_size = out.w;
        jfloat *output[output_size];
        for (int j = 0; j < out.w; j++) {
            output[j] = &out[j];
        }

        jfloatArray jOutputData = env->NewFloatArray(output_size);
        if (jOutputData == nullptr) return nullptr;
        env->SetFloatArrayRegion(jOutputData, 0, output_size,
                                 reinterpret_cast<const jfloat *>(*output));  // copy

        return jOutputData;
    }
}
}