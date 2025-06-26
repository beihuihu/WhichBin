package com.example.whichbin.database

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues

import kotlin.jvm.internal.Intrinsics

import java.util.ArrayList

import android.database.sqlite.SQLiteDatabase as SQLiteDatabase




object DatabaseUtil {

    //干垃圾
    val notrash1 = arrayListOf(
        "玻璃胶",
        "液体胶",
        "洗脸巾",
        "洗碗布",
        "橡胶手套",
        "污损纸张",
        "快递单",
        "碎纸屑",
        "油条包装袋",
        "车票",
        "便签纸",
        "电蚊香片",
        "丝袜",
        "牙齿",
        "脚趾甲",
        "牙刷",
        "网球",
        "塑料花",
        "假睫毛",
        "隐形眼镜",
        "成人用品",
        "避孕套",
        "荧光笔",
        "油性笔",
        "马桶刷",
        "剃须刀",
        "打火机",
        "油画刷",
        "油画颜料",
        "园艺土",
        "浴沙",
        "粘土",
        "仓鼠垫沙",
        "泥巴",
        "粘虫板",
        "蟑螂贴",
        "遮光布",
        "纸胶带",
        "自煮火锅壳",
        "滤网",
        "滤挂咖啡",
        "茶包",
        "眉笔",
        "眉粉",
        "美妆蛋",
        "密封袋",
        "墨镜",
        "内衣",
        "暖宝宝",
        "铅笔",
        "巧克力锡纸",
        "巧克力包装袋",
        "保险冰袋",
        "冰棍棒",
        "大米袋",
        "带油抹布",
        "拖把",
        "吸管",
        "奶茶杯",
        "席子",
        "口罩",
        "头绳",
    )
    //湿垃圾
    val notrash2 = arrayListOf(
        "鱼虾",
        "鱼鳞",
        "虾",
        "大闸蟹",
        "大闸蟹壳",
        "鱿鱼丝",
        "苹果核",
        "西瓜子",
        "木瓜",
        "南瓜",
        "食品添加剂",
        "豆制品",
        "动物内脏",
        "肉类",
        "炸鸡",
        "鸭脖",
        "巧克力",
        "糖果",
        "奶粉",
        "咖啡粉",
        "糖",
        "盐",
        "味精",
        "鸡精",
        "面条",
        "意大利面",
        "荞麦面",
        "燕麦",
        "薏米",
        "荞麦",
        "菌菇",
        "金针菇",
        "银耳",
        "香菇",
        "酱料",
        "番茄酱",
        "梅干菜",
        "紫菜",
        "鲜花",
        "百合",
        "蔷薇花",
        "火锅底料",
        "火锅渣",
        "废弃食用油",
        "榴莲酥",
        "果冻",
        "膨化食品",
        "猫粮",
        "青椒",
        "螺丝肉",
        "饮料",
        "鱼豆腐",
        "奶酪",
        "年糕",
        "果皮果肉"
    )
    //可回收
    val notrash3 = arrayListOf(
        "化妆品外包装纸盒",
        "不锈钢保温杯",
        "保鲜膜内芯",
        "废鼠标",
        "麦克风",
        "电子产品",
        "手机",
        "笔记本电脑",
        "平板电脑",
        "ipad",
        "复印机",
        "计算器",
        "充电牙刷",
        "蓝牙耳机",
        "吸尘器",
        "洗衣机",
        "健身器材",
        "家电",
        "电饭煲",
        "咖啡机",
        "搓衣板",
        "美工刀",
        "剪刀",
        "排球",
        "皮球",
        "皮箱",
        "八音盒",
        "磁铁",
        "单车",
        "铁丝",
        "钥匙",
        "望远镜",
        "文件柜",
        "鞋带",
        "滑板",
        "加湿器",
        "金属晾衣杆",
        "开关",
        "拉杆箱",
        "铃铛",
        "针织品",
        "被套",
        "被褥",
        "毛毯",
        "旧窗帘",
        "眼镜布",
        "浴帘",
        "真空压缩袋",
        "指甲锉",
        "废锁头",
        "订书机",
        "陶瓷器皿",
        "洗护用品",
        "锅"
    )
    //不可回收垃圾
    val notrash4 = arrayListOf(
        "蓄电池",
        "电瓶",
        "手机电池",
        "纽扣电池",
        "废油漆",
        "油漆刷",
        "化学剂桶",
        "感光胶片",
        "录像带",
        "卸甲水",
        "工业胶水",
        "指甲油",
        "染发剂包装盒"
    )

    //补充数据集
    val trash = listOf(
        //0-10 其他垃圾
        listOf("一次性餐具", "一次性塑料盘", "一次性纸杯"),
        listOf("化妆品瓶", "脱毛膏"),
        listOf("卫生纸", "鼻涕纸", "厕纸"),
        listOf("尿片", "脏纸尿裤"),
        listOf("污损塑料", "一次性保鲜膜", "垃圾袋"),
        listOf("烟蒂"),
        listOf("牙签", "牙线", "竹签"),
        listOf("破碎花盆及碟碗", "花盆"),
        listOf("竹筷","筷子"),
        listOf("纸杯", "爆米花桶"),
        listOf("贝壳"),
        //11-22 厨余垃圾
        listOf("剩菜剩饭", "剩饭剩菜","粥", "粽子馅", "煲仔饭"),
        listOf("大骨头"),
        listOf("果壳瓜皮", "洋葱皮", "毛豆壳", "豌豆皮"),
        listOf("残枝落叶"),
        listOf("水果果皮", "香蕉皮", "芒果皮", "葡萄皮"),
        listOf("水果果肉", "苹果", "香蕉", "甘蔗", "琵琶", "山楂", "蔓越莓", "红枣"),
        listOf("茶叶渣", "白茶"),
        listOf("菜梗菜叶", "玉米须", "葱", "大白菜"),
        listOf("落叶"),
        listOf("蛋壳"),
        listOf("西餐糕点", "蛋糕", "饼干", "面包", "蛋挞", "鸡蛋仔", "苹果派"),
        listOf("鱼骨"),
        //23-51 可回收垃圾
        listOf("传单"),
        listOf("充电宝"),
        listOf("包", "帆布包", "背包"),
        listOf("塑料玩具", "玩具枪"),
        listOf("塑料碗盆"),
        listOf("塑料衣架"),
        listOf("快递纸袋"),
        listOf("报纸"),
        listOf("插头电线", "网线"),
        listOf("旧书", "课本","书籍纸张"),
        listOf("旧衣服"),
        listOf("易拉罐"),
        listOf("杂志", "明信片"),
        listOf("枕头"),
        listOf("毛绒玩具"),
        listOf("泡沫塑料"),
        listOf("洗发水瓶"),
        listOf("牛奶盒等利乐包装"),
        listOf("玻璃", "玻璃碎片"),
        listOf("玻璃瓶罐", "玻璃花瓶", "香水瓶", "玻璃沙漏", "墨水瓶","玻璃器皿"),
        listOf("皮鞋", "童鞋", "旧布鞋"),
        listOf("砧板"),
        listOf("纸板箱"),
        listOf("调料瓶"),
        listOf("酒瓶"),
        listOf("金属食品罐", "茶叶铁盒", "金属厨具", "铝罐"),
        listOf( "平底锅"),//"锅",
        listOf("食用油桶"),
        listOf("饮料瓶", "汽水瓶"),
        //52-59 有害垃圾
        listOf("干电池"),
        listOf("废弃水银温度计", "废血压计"),
        listOf("废旧灯管灯泡", "荧光灯", "卤素灯", "LED灯（含水银）"),
        listOf("杀虫剂容器", "老鼠药"),
        listOf("电池"),
        listOf("软膏"),
        listOf("过期药物", "止痛膏", "开塞露", "玻璃药瓶", "药罐", "眼药水", "过期的胶囊药物", "胶囊", "滴眼液", "烫伤膏", "烫伤药"),
        listOf("除草剂容器", "农药瓶")
    )

    public fun addDataShanghai(context: Context){
        val dbHelper = GarbageDatabase(context,"RubDatabase.db",1)
        val db=dbHelper.writableDatabase
        for(i in 0..10)
        {
            for (put in trash[i]) {
                val value=ContentValues().apply {
                put("name", put)
                put("id",i)
                put("type", "干垃圾")}
                db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        
        for(i in notrash1) {
            val value=ContentValues().apply {
           put("name", i)
           put("id", -1)
           put("type", "干垃圾")}
            db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }

        for(i in 11..22)
        {
            for (put2 in trash[i]) {
                val value=ContentValues().apply {
                put("name", put2)
                put("id", Integer.valueOf(i))
                put("type", "湿垃圾")}
                db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash2) {
            val value = ContentValues().apply {
            put("name", i)
            put("id", -2)
            put("type", "湿垃圾")}
            db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }
        for(i in 23..51)
        {
            for (put3 in trash[i]) {
                val value = ContentValues().apply {
                put("name", put3)
                put("id", i)
                put("type", "可回收物")}
                db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash3) {
            val value = ContentValues().apply {
            put("name", i)
            put("id", -3)
            put("type", "可回收物")}
            db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }
        for(i in 52..59)
        {
            for (put4 in trash[i]) {
                val value = ContentValues().apply {
                put("name", put4)
                put("id", i)
                put("type", "有害垃圾")}
                db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash4) {
            val value = ContentValues().apply {
            put("name", i)
            put("id", -3)
            put("type", "有害垃圾")}
            db.insertWithOnConflict("ShanghaiTable", null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }
    }

    public fun addDataChengdu(context: Context){
        val dbHelper = GarbageDatabase(context,"RubDatabase.db",1)
        val db=dbHelper.writableDatabase
        for(i in 0..10)
        {
            for (put in trash[i]) {
                val value=ContentValues().apply {
                    put("name", put)
                    put("id",i)
                    put("type", "其他垃圾")}
              db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }

        for(i in notrash1) {
            val value=ContentValues().apply {
                put("name", i)
                put("id", -1)
                put("type", "其他垃圾")}
          db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }

        for(i in 11..22)
        {
            for (put2 in trash[i]) {
                val value=ContentValues().apply {
                    put("name", put2)
                    put("id", Integer.valueOf(i))
                    put("type", "厨余垃圾")}
                db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash2) {
            val value = ContentValues().apply {
                put("name", i)
                put("id", -2)
                put("type", "厨余垃圾")}
            db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }

        for(i in 23..51)
        {
            for (put3 in trash[i]) {
                val value = ContentValues().apply {
                    put("name", put3)
                    put("id", i)
                    put("type", "可回收物")}
                db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash3) {
            val value = ContentValues().apply {
                put("name", i)
                put("id", -3)
                put("type", "可回收物")}
            db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }
        for(i in 52..59)
        {
            for (put4 in trash[i]) {
                val value = ContentValues().apply {
                    put("name", put4)
                    put("id", i)
                    put("type", "有害垃圾")}
                db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
        for(i in notrash4) {
            val value = ContentValues().apply {
                put("name", i)
                put("id", -3)
                put("type", "有害垃圾")}
            db.insertWithOnConflict("ChengduTable",null, value,SQLiteDatabase.CONFLICT_IGNORE)
        }

    }

    @SuppressLint("Range")
    public fun foundResult(context: Context, garbageName:String, curplace:String):ArrayList<search_result>{
        val dbHelper = GarbageDatabase(context,"RubDatabase.db",1)
        val db=dbHelper.writableDatabase
        val searchList = ArrayList<search_result>()

        var city = ""
        if(curplace == "上海市")
            city = "ShanghaiTable"
        else
            city = "ChengduTable"

        val cursor=db.rawQuery("select * from $city where name like '%$garbageName%'",null)
        if(cursor.moveToFirst()){
            do{
                val name=cursor.getString(cursor.getColumnIndex("name"))
                val type=cursor.getString(cursor.getColumnIndex("type"))
                val i=cursor.getInt(cursor.getColumnIndex("id"))
                searchList.add(search_result(name,type,i))
            }while (cursor.moveToNext())
        }
        return  searchList


    }

    @SuppressLint("Range")
    fun foundGarbage(context: Context, str: String): ArrayList<search_result> {
        val dbHelper = GarbageDatabase(context,"RubDatabase.db",1)
        val db=dbHelper.writableDatabase
        val arrayList = ArrayList<search_result>()

        val rawQuery = db.rawQuery("select * from ChengduTable where type like '%$str%'", null)
        if (rawQuery.moveToFirst()) {
            do {
                val string = rawQuery.getString(rawQuery.getColumnIndex("name"))
                val string2 = rawQuery.getString(rawQuery.getColumnIndex("type"))
                val i = rawQuery.getInt(rawQuery.getColumnIndex("id"))
                Intrinsics.checkNotNullExpressionValue(string, "name")
                Intrinsics.checkNotNullExpressionValue(string2, "type")
                arrayList.add(search_result(string, string2, i))
            } while (rawQuery.moveToNext())
        }
        return arrayList
    }


}