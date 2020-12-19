package com.carswaddle.carswaddleandroid.stripe

import com.carswaddle.carswaddleandroid.retrofit.*


private const val stripeStagingPublishableKey = "pk_test_93FPMcPQ4mSaWfjtMWlkGvDr00ytb8KnDJ"
private const val stripeProductionPublishableKey = "pk_live_ZJkoNBROBK0ttmZLDNfNF0Cw00VwQ7JjFw"

fun stripePublishableKey(): String {
    return when(server) {
        Server.staging -> stripeStagingPublishableKey
        Server.production -> stripeProductionPublishableKey
        Server.local -> stripeStagingPublishableKey
    }
}