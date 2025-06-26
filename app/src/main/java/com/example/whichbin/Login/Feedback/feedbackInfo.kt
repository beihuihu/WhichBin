package com.example.garbageclassification.Feedback

import kotlin.jvm.internal.Intrinsics
class feedbackInfo(i: Int, str: String) {
    var id: Int
    private var name: String
    operator fun component1(): Int {
        return id
    }

    operator fun component2(): String {
        return name
    }

    fun copy(i: Int, str: String): feedbackInfo {
        Intrinsics.checkNotNullParameter(str, "name")
        return feedbackInfo(i, str)
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj !is feedbackInfo) {
            return false
        }
        val feedbackinfo = obj
        return id == feedbackinfo.id && Intrinsics.areEqual(name as Any, feedbackinfo.name as Any)
    }

    override fun hashCode(): Int {
        return Integer.hashCode(id) * 31 + name.hashCode()
    }

    override fun toString(): String {
        return "feedbackInfo(id=" + id + ", name=" + name + ')'
    }

    fun getName(): String {
        return name
    }

    fun setName(str: String) {
        name = str
    }

    fun feed(i: Int, str: String) {
        id = i
        name = str
    }

    companion object {
        fun  /* synthetic */`copy$default`(
            feedbackinfo: feedbackInfo,
            i: Int,
            str: String,
            i2: Int,
            obj: Any?
        ): feedbackInfo {
            var i = i
            var str = str
            if (i2 and 1 != 0) {
                i = feedbackinfo.id
            }
            if (i2 and 2 != 0) {
                str = feedbackinfo.name
            }
            return feedbackinfo.copy(i, str)
        }
    }

    init {
        id = i
        name = str
    }
}