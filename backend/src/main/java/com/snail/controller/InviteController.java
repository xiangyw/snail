package com.snail.controller;

import com.snail.entity.InviteCode;
import com.snail.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invite")
@RequiredArgsConstructor
@Tag(name = "Invite API", description = "邀请码相关API")
public class InviteController {
    
    private final InviteService inviteService;
    
    /**
     * 生成邀请码
     */
    @PostMapping("/generate")
    @Operation(summary = "生成邀请码", description = "为当前用户生成一个新的邀请码")
    public ResponseEntity<InviteCode> generateInviteCode(
            @RequestParam @NotNull(message = "用户ID不能为空") Long userId) {
        
        InviteCode inviteCode = inviteService.generateInviteCode(userId);
        return ResponseEntity.ok(inviteCode);
    }
    
    /**
     * 使用邀请码
     */
    @PostMapping("/use")
    @Operation(summary = "使用邀请码", description = "用户使用邀请码进行注册或关联")
    public ResponseEntity<Boolean> useInviteCode(
            @RequestParam @NotBlank(message = "邀请码不能为空") String code,
            @RequestParam @NotNull(message = "被邀请用户ID不能为空") Long invitedUserId) {
        
        boolean success = inviteService.useInviteCode(code, invitedUserId);
        return ResponseEntity.ok(success);
    }
    
    /**
     * 获取用户邀请码列表
     */
    @GetMapping("/codes/{userId}")
    @Operation(summary = "获取用户邀请码", description = "获取指定用户的所有邀请码")
    public ResponseEntity<List<InviteCode>> getUserInviteCodes(
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        
        List<InviteCode> codes = inviteService.getUserInviteCodes(userId);
        return ResponseEntity.ok(codes);
    }
    
    /**
     * 获取用户邀请统计
     */
    @GetMapping("/stats/{userId}")
    @Operation(summary = "获取用户邀请统计", description = "获取指定用户的邀请统计数据")
    public ResponseEntity<InviteService.InviteStats> getUserInviteStats(
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        
        InviteService.InviteStats stats = inviteService.getInviteStats(userId);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 发放邀请奖励
     */
    @PostMapping("/reward/{recordId}")
    @Operation(summary = "发放邀请奖励", description = "为邀请记录发放奖励")
    public ResponseEntity<Boolean> grantInviteReward(
            @PathVariable @NotNull(message = "记录ID不能为空") Long recordId) {
        
        boolean success = inviteService.grantReward(recordId);
        return ResponseEntity.ok(success);
    }
}