package com.carswaddle.carswaddleandroid.services

//val cd = ContentType()

data class ContentType(val rawValue: String) {



    companion object {

        public const val headerPrefix = "Content-Type: "

        public const val applicationFormURLEncoded = "application/x-www-form-urlencoded"
        public const val applicationOctetStream = "application/octet-stream"
        public const val applicationJSON = "application/json"
        public const val applicationZIP = "application/zip"
        public const val imageJPEG = "image/jpeg"
        public const val imagePNG = "image/png"
        public const val textHTML = "text/html;charset=utf-8"
        public const val any = "*/*"

        public fun multipartFormContentType(boundary: String): ContentType {
            return ContentType("multipart/form-data; boundary= " + boundary)
        }
    }
}