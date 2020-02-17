package com.lgorev.ksuonlineeducation.util

import java.time.LocalDate

operator fun ClosedRange<LocalDate>.iterator() : Iterator<LocalDate>{
    return object: Iterator<LocalDate> {
        private var next = this@iterator.start
        private val finalElement = this@iterator.endInclusive
        private var hasNext = !next.isAfter(this@iterator.endInclusive)

        override fun hasNext(): Boolean = hasNext

        override fun next(): LocalDate {
            val value = next
            if(value == finalElement) hasNext = false
            else next = next.plusDays(1)
            return value
        }
    }
}

inline fun ClosedRange<LocalDate>.filter(predicate: (LocalDate) -> Boolean): MutableList<LocalDate> {
    val list = mutableListOf<LocalDate>()
    for (element in this) if(predicate(element)) list.add(element)
    return list
}
