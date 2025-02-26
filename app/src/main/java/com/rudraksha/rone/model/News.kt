package com.rudraksha.rone.model

data class NewsResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<Article> = listOf()
)

data class Article(
    val source: Source = Source(),
    val author: String? = null,
    val title: String = "",
    val description: String? = null,
    val url: String = "",
    val urlToImage: String? = null,
    val publishedAt: String = "",
    val content: String? = null
)

data class Source(
    val id: String? = null,
    val name: String = ""
)

/*
{
  "status": "ok",
  "totalResults": 2,
  "articles": [
    {
      "source": { "id": "cnn", "name": "CNN" },
      "author": "John Doe",
      "title": "Breaking News",
      "description": "Some news description",
      "url": "https://example.com/news1",
      "urlToImage": "https://example.com/news1.jpg",
      "publishedAt": "2025-01-28T12:00:00Z",
      "content": "Full news content goes here."
    },
    {
      "source": { "id": "bbc", "name": "BBC" },
      "author": "Jane Smith",
      "title": "Another News",
      "description": "Another news description",
      "url": "https://example.com/news2",
      "urlToImage": "https://example.com/news2.jpg",
      "publishedAt": "2025-01-28T13:00:00Z",
      "content": "Another full news content goes here."
    }
  ]
}

 */