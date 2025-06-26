package com.example.whichbin.Login.Feedback

import android.content.Context
import android.graphics.BitmapFactory
import kotlin.collections.arrayListOf
import com.example.whichbin.BaseActivity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.example.whichbin.R
import kotlinx.android.synthetic.main.activity_feedback.*
import okhttp3.*
import java.io.File
import android.widget.ArrayAdapter
import android.text.Editable

import android.text.TextWatcher


class FeedbackActivity : BaseActivity() {
    lateinit var context :Context
    var imageUri: Uri? = null
    val trashList = arrayListOf(
        "传单",
        "充电宝",
        "包",
        "塑料玩具",
        "塑料碗盆",
        "塑料衣架",
        "快递纸袋",
        "报纸",
        "插头电线",
        "旧书",
        "旧衣服",
        "易拉罐",
        "杂志",
        "枕头",
        "毛绒玩具",
        "泡沫塑料",
        "洗发水瓶",
        "牛奶盒等利乐包装",
        "玻璃",
        "玻璃瓶罐",
        "皮鞋",
        "砧板",
        "纸板箱",
        "调料瓶",
        "酒瓶",
        "金属食品罐",
        "锅",
        "食用油桶",
        "饮料瓶",
        "帆布包",
        "玻璃花瓶",
        "玻璃碎片",
        "被套",
        "不锈钢保温杯",
        "保鲜膜内芯",
        "茶叶铁盒",
        "金属厨具",
        "被褥",
        "废鼠标",
        "电子产品",
        "铝罐",
        "麦克风",
        "毛毯",
        "美工刀",
        "明信片",
        "墨水瓶",
        "排球",
        "皮球",
        "皮箱",
        "平底锅",
        "汽水瓶",
        "八音盒",
        "背包",
        "笔记本电脑",
        "玻璃沙漏",
        "充电牙刷",
        "磁铁",
        "搓衣板",
        "单车",
        "铁丝",
        "童鞋",
        "玩具枪",
        "网线",
        "望远镜",
        "文件柜",
        "吸尘器",
        "洗衣机",
        "香水瓶",
        "鞋带",
        "滑板",
        "化妆品外包装纸盒",
        "计算器",
        "加湿器",
        "剪刀",
        "健身器材",
        "金属晾衣杆",
        "旧布鞋",
        "旧窗帘",
        "咖啡机",
        "开关",
        "课本",
        "拉杆箱",
        "蓝牙耳机",
        "铃铛",
        "眼镜布",
        "浴帘",
        "真空压缩袋",
        "指甲锉",
        "复印机",
        "废锁头",
        "手机",
        "电饭煲",
        "钥匙",
        "订书机",
        "干电池",
        "废弃水银温度计",
        "废旧灯管灯泡",
        "杀虫剂容器",
        "电池",
        "软膏",
        "过期药物",
        "除草剂容器",
        "电瓶",
        "纽扣电池",
        "手机电池",
        "废油漆",
        "农药瓶",
        "荧光灯",
        "卤素灯",
        "感光胶片",
        "指甲油",
        "油漆刷",
        "止痛膏",
        "录像带",
        "卸甲水",
        "开塞露",
        "废血压计",
        "工业胶水",
        "染发剂包装盒",
        "烫伤膏",
        "烫伤药",
        "手机电池",
        "LED灯（含水银）",
        "蓄电池",
        "玻璃药瓶",
        "老鼠药",
        "药罐",
        "化学剂桶",
        "眼药水",
        "过期的胶囊药物",
        "胶囊",
        "滴眼液",
        "剩菜剩饭",
        "大骨头",
        "果壳瓜皮",
        "残枝落叶",
        "水果果皮",
        "水果果肉",
        "茶叶渣",
        "菜梗菜叶",
        "落叶",
        "蛋壳",
        "西餐糕点",
        "鱼骨",
        "鱼虾",
        "鱼鳞",
        "饼干",
        "蛋糕",
        "面包",
        "虾",
        "粥",
        "苹果核",
        "香蕉皮",
        "芒果皮",
        "葡萄皮",
        "洋葱皮",
        "红枣",
        "香菇",
        "大白菜",
        "粽子馅",
        "食品添加剂",
        "面条",
        "豆制品",
        "动物内脏",
        "巧克力",
        "糖果",
        "猫粮",
        "番茄酱",
        "甘蔗",
        "糖",
        "盐",
        "鲜花",
        "榴莲酥",
        "废弃食用油",
        "火锅底料",
        "螺丝肉",
        "毛豆壳",
        "玉米须",
        "山楂",
        "鸭脖",
        "燕麦",
        "果冻",
        "西瓜子",
        "金针菇",
        "炸鸡",
        "大闸蟹",
        "酱料",
        "饮料",
        "意大利面",
        "鱿鱼丝",
        "蔓越莓",
        "木瓜",
        "葱",
        "豌豆皮",
        "味精",
        "鸡精",
        "菌菇",
        "青椒",
        "火锅渣",
        "奶粉",
        "蛋挞",
        "鸡蛋仔",
        "大闸蟹壳",
        "肉类",
        "薏米",
        "银耳",
        "鱼豆腐",
        "梅干菜",
        "奶酪",
        "南瓜",
        "年糕",
        "膨化食品",
        "琵琶",
        "苹果",
        "苹果派",
        "香蕉",
        "蔷薇花",
        "荞麦",
        "白茶",
        "百合",
        "煲仔饭",
        "咖啡粉",
        "紫菜",
        "一次性餐具",
        "化妆品瓶",
        "卫生纸",
        "尿片",
        "污损塑料",
        "烟蒂",
        "牙签",
        "破碎花盆及碟碗",
        "竹筷",
        "纸杯",
        "贝壳",
        "玻璃胶",
        "洗脸巾",
        "花盆",
        "橡胶手套",
        "脏纸尿裤",
        "快递单",
        "一次性塑料盘",
        "电蚊香片",
        "丝袜",
        "牙齿",
        "牙刷",
        "一次性纸杯",
        "网球",
        "塑料花",
        "假睫毛",
        "洗碗布",
        "巧克力包装袋",
        "鼻涕纸",
        "隐形眼镜",
        "避孕套",
        "牙线",
        "污损纸张",
        "荧光笔",
        "马桶刷",
        "碎纸屑",
        "竹签",
        "剃须刀",
        "厕纸",
        "打火机",
        "液体胶",
        "一次性保鲜膜",
        "油画刷",
        "油画颜料",
        "油条包装袋",
        "油性笔",
        "园艺土",
        "浴沙",
        "粘虫板",
        "粘土",
        "蟑螂贴",
        "遮光布",
        "纸胶带",
        "自煮火锅壳",
        "滤网",
        "滤挂咖啡",
        "眉笔",
        "眉粉",
        "美妆蛋",
        "密封袋",
        "墨镜",
        "内衣",
        "奶茶杯",
        "泥巴",
        "暖宝宝",
        "铅笔",
        "巧克力锡纸",
        "保险冰袋",
        "贝壳",
        "爆米花桶",
        "便签纸",
        "冰棍棒",
        "仓鼠垫沙",
        "车票",
        "成人用品",
        "大米袋",
        "带油抹布",
        "拖把",
        "脱毛膏",
        "席子",
        "脚趾甲",
        "口罩",
        "垃圾袋",
        "茶包",
        "头绳",
        "吸管"
    )

    /* access modifiers changed from: protected */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_feedback as Int)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, trashList)
       // val feedbackAdapter = FeedbackAdapter(this, R.layout.feedback_item, trashList)
        //feedbackAdapter.notifyDataSetChanged()
        etGarName.setAdapter(adapter)
        imageUri = Uri.parse(intent.getStringExtra("Uri"))
        imageUri?.let{
            tvimage.setImageBitmap(getBitmapFromUri(it))
        }

        SubmitBtn.setOnClickListener {
            Toast.makeText(this, "上传成功，获得1个积分！", Toast.LENGTH_SHORT).show()
        }

        feed_back.setOnClickListener{
            finish()
        }
    }

    fun getBitmapFromUri(uri:Uri)=contentResolver.openFileDescriptor(uri,"r")?.use{
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }


    private fun submitImage(file: File, i: Int, str: String) {
        val createFormData: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.getName(),
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )

        //        (ServiceCreater.creat<ClassicService>(ClassicService::class.java,this).submite(i, str, createFormData)
//            .enqueue(){
//        override fun onResponse(call: Call<submitresult?>, response: Response<submitresult?>) {
//            val body: submitresult? = response.body()
//            if (body != null) {
//                val feedbackActivity = this
//                if (body.getStatus()== "ok") {
//                    Toast.makeText(this, "上传成功,获得1个积分！", 0).show()
//                    //userData.INSTANCE.setCredits(body.getCredits())
//                    return
//                }
//                Toast.makeText(this, "上传失败", 0).show()
//            }
//        }
//        override fun onFailure(call: Call<submitresult?>, th: Throwable) {
//            Toast.makeText(this, "无法访问服务器", 0).show()
//        }
//    }
    }


}