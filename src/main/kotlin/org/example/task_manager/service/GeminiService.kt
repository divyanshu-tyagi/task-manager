package org.example.task_manager.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiResponse(val candidates: List<GeminiCandidate>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiCandidate(val content: GeminiContent)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiContent(val parts: List<GeminiPart>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiPart(val text: String)

@Service
class GeminiService(
    @Value("\${spring.gemini.api-key}") private val apiKey: String,
    @Value("\${spring.gemini.url}") private val apiUrl: String
) {
    private val webClient = WebClient.builder().build()

    fun ask(prompt: String): String{
        val requestBody = mapOf(
            "contents" to listOf(
                mapOf(
                   "parts" to listOf(
                       mapOf("text" to prompt)
                   )
                )
            )
        )
        val response = webClient.post()
            .uri("$apiUrl?key=$apiKey")
            .header("content-type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(GeminiResponse::class.java)
            .block()

        return response?.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()
            ?.text ?: throw RuntimeException("No response from Gemini")
    }

    fun suggestPriority(title: String): String{
        val prompt = """
            You are a task management assistant.
            Based on this task title: "$title"
            Suggest a priority level. Reply with ONLY one word: HIGH, MEDIUM, or LOW.
            No explanation, just the single word.
        """.trimIndent()
        return ask(prompt).trim().uppercase()
    }

    fun enhanceTask(title: String, currentDescription: String?): String {
        val prompt = """
            You are a task management assistant.
            Task title: "$title"
            Current description: "${currentDescription ?: "none"}"
            
            Write a clear, concise task description in 2-3 sentences.
            Include what needs to be done, why it matters, and any key considerations.
            Reply with ONLY the description text, nothing else.
        """.trimIndent()

        return ask(prompt).trim()
    }
}