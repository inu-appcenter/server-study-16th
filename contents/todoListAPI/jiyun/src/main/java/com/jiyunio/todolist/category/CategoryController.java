package com.jiyunio.todolist.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category{memberId}")
    public ResponseEntity<String> createCategory(@PathVariable Long memberId, @RequestParam String categoryName) {
        categoryService.createCategory(memberId, categoryName);
        return ResponseEntity.ok("카테고리 생성 성공");
    }

    @GetMapping("/category/{memberId}")
    public List<String> getCategory(@PathVariable Long memberId) {
        return categoryService.getCategory(memberId);
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestParam String categoryName) {
        categoryService.updateCategory(categoryId, categoryName);
        return ResponseEntity.ok("카테고리 수정 성공");
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리 삭제 성공");
    }
}
