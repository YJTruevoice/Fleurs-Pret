package com.arthur.lib

class TestThirdMaxNum {

    fun findThirdMaxNum(arr: Array<Int>): Int {
        var firstMax = Int.MIN_VALUE
        var secMax = Int.MIN_VALUE
        var thirdMax = Int.MIN_VALUE
        if (arr.size >= 3) {
            for (num in arr) {
                if (num > firstMax) {
                    thirdMax = secMax
                    secMax = firstMax
                    firstMax = num
                } else if (num in (secMax + 1) until firstMax) {
                    thirdMax = secMax
                    secMax = num
                } else if (num in (thirdMax + 1) until secMax) {
                    thirdMax = num
                }
            }
        }

        return thirdMax
    }
}