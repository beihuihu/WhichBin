package com.example.whichbin.http

data class Scores(
    val labelid: Int,
    val labels: String,
    val probability: Double,
    val scores: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other != scores) return false

        other as Scores

        if (labelid != other.labelid) return false
        if (labels != other.labels) return false
        if (probability != other.probability) return false
        if (!scores.contentEquals(other.scores)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = labelid
        result = 31 * result + labels.hashCode()
        result = 31 * result + probability.hashCode()
        result = 31 * result + scores.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "scores(labelid=$labelid, labels='$labels', probability=$probability, scores=${scores.contentToString()})"
    }
}