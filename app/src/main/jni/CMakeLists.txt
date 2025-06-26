project("whichbin")

cmake_minimum_required(VERSION 3.10.2)

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20220216-android/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)
#set(ncnn_lib ${CMAKE_SOURCE_DIR}/src/main/jniLibs/armeabi-v7a/libncnn.a)
add_library (mobileNet SHARED mobileNet_jni.cpp)
#set_target_properties(ncnn_lib PROPERTIES IMPORTED_LOCATION ${ncnn_lib})
#add_library(ncnn_jni SHARED ncnn_jni.cpp)
#find_library(log-lib log)
#target_link_libraries( ncnn_jni ncnn_lib jnigraphics ${log-lib})
target_link_libraries(mobileNet ncnn jnigraphics)