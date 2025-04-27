//package com.example.constants
//
//import com.example.constants.Result
//import com.example.constants.Result.*
//import com.google.gson.*
//import java.lang.reflect.Type
//
//class ResultTypeAdapter : JsonDeserializer<Result<*>> {
//    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Result<*> {
//        val jsonObject = json?.asJsonObject
//
//        // If the result is in a "success" format
//        if (jsonObject?.has("data") == true) {
//            val data = jsonObject.get("data").asJsonObject
//            return Success(data)
//        }
//        // Handle the case for Error (you can customize it according to your Error structure)
//        else if (jsonObject?.has("error") == true) {
//            val errorMessage = jsonObject.get("error").asString
//            return Error(errorMessage)
//        }
//        // Default case for loading
//        else {
//            return Loading()
//        }
//    }
//}
