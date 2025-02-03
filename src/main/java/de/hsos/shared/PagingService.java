package de.hsos.shared;

import java.util.Collections;
import java.util.List;

public class PagingService {
    public static List<ArticleDTO> getPagedArticleList(int page, int pageSize, List<ArticleDTO> articles){
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be at least 1.");
        }
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, articles.size());
        if (fromIndex >= articles.size()) {
            return Collections.emptyList(); // Return empty list if page exceeds available items
        }

        return articles.subList(fromIndex, toIndex);
    }
}
