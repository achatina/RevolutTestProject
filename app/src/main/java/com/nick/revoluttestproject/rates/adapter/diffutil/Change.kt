package com.nick.revoluttestproject.rates.adapter.diffutil

import com.nick.revoluttestproject.rates.model.Rate

data class Change(
    val oldData: Rate,
    val newData: Rate
)

fun createCombinedPayload(payloads: List<Change>): Change? {
    if (payloads.isEmpty()) return null

    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}