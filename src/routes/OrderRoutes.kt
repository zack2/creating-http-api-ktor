package com.olivierloukombo.routes

import com.olivierloukombo.models.orderStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

// get the order
fun Route.getOrderRoute() {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        call.respond(order)
    }
}

//list of orders
fun Route.listOrdersRoute() {
    get("/orders") {
        if (orderStorage.isNotEmpty()) call.respond(orderStorage)
    }
}

//total orders
fun Route.totalizeOrderRoute() {
    get("/order/{id}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request",
            status = HttpStatusCode.NotFound)

        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        val total = order.contents.map { it.price * it.amount }.sum()
        call.respond(total)
    }
}

fun Application.registerOrderRoute() {
    routing {
        getOrderRoute()
        listOrdersRoute()
        totalizeOrderRoute()
    }
}