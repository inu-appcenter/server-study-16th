package com.jiyunio.todolist.category;

import com.jiyunio.todolist.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Category", description = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category{memberId}")
    @Operation(summary = "카테고리 생성")
    public ResponseEntity<ResponseDTO> createCategory(@Parameter(description = "member의 id") @PathVariable Long memberId, @RequestParam @NotBlank String categoryName) {
        categoryService.createCategory(memberId, categoryName);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 생성 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/category/{memberId}")
    @Operation(summary = "카테고리 조회")
    public List<GetCategoryDTO> getCategory(@Parameter(description = "member의 id") @PathVariable Long memberId) {
        return categoryService.getCategory(memberId);
    }

    @PutMapping("/category/{categoryId}")
    @Operation(summary = "카테고리 수정")
    public ResponseEntity<ResponseDTO> updateCategory(@Parameter(description = "카테고리의 id") @PathVariable Long categoryId, @RequestParam @NotBlank String categoryName) {
        categoryService.updateCategory(categoryId, categoryName);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 수정 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/category/{categoryId}")
    @Operation(summary = "카테고리 삭제")
    public ResponseEntity<ResponseDTO> deleteCategory(@Parameter(description = "카테고리의 id") @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 삭제 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }
}
