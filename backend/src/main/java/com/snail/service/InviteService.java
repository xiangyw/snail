package com.snail.service;

import com.snail.entity.InviteCode;
import com.snail.entity.InviteRecord;
import com.snail.repository.InviteCodeRepository;
import com.snail.repository.InviteRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "Invite Service", description = "邀请码服务")
public class InviteService {
    
    private final InviteCodeRepository inviteCodeRepository;
    private final InviteRecordRepository inviteRecordRepository;
    
    /**
     * 为用户生成邀请码
     */
    @Operation(summary = "生成邀请码", description = "为指定用户生成一个新的邀请码")
    public InviteCode generateInviteCode(@Parameter(description = "用户ID") Long userId) {
        // 检查用户是否已有未使用的邀请码
        long unusedCount = inviteCodeRepository.countUnusedCodesByUserId(userId);
        if (unusedCount >= 5) { // 假设每个用户最多拥有5个未使用的邀请码
            throw new RuntimeException("用户已达最大邀请码数量限制");
        }
        
        InviteCode inviteCode = new InviteCode();
        inviteCode.setUserId(userId);
        return inviteCodeRepository.save(inviteCode);
    }
    
    /**
     * 使用邀请码
     */
    @Transactional
    @Operation(summary = "使用邀请码", description = "用户使用邀请码注册或关联账户")
    public boolean useInviteCode(
            @Parameter(description = "邀请码") String code,
            @Parameter(description = "被邀请用户ID") Long invitedUserId) {
        
        Optional<InviteCode> inviteCodeOpt = inviteCodeRepository.findByCode(code);
        if (inviteCodeOpt.isEmpty()) {
            log.warn("邀请码不存在: {}", code);
            return false;
        }
        
        InviteCode inviteCode = inviteCodeOpt.get();
        if (inviteCode.getIsUsed()) {
            log.warn("邀请码已被使用: {}", code);
            return false;
        }
        
        // 更新邀请码状态
        inviteCode.setIsUsed(true);
        inviteCode.setUsedAt(LocalDateTime.now());
        inviteCode.setUsedByUserId(invitedUserId);
        inviteCodeRepository.save(inviteCode);
        
        // 创建邀请记录
        InviteRecord record = new InviteRecord();
        record.setInviterUserId(inviteCode.getUserId());
        record.setInvitedUserId(invitedUserId);
        record.setInviteCode(code);
        record.setRewardGranted(false); // 初始不发放奖励
        inviteRecordRepository.save(record);
        
        log.info("邀请码成功使用: {} 由用户 {} 使用", code, invitedUserId);
        return true;
    }
    
    /**
     * 发放邀请奖励
     */
    @Transactional
    @Operation(summary = "发放邀请奖励", description = "为邀请者和被邀请者发放奖励")
    public boolean grantReward(@Parameter(description = "邀请记录ID") Long recordId) {
        Optional<InviteRecord> recordOpt = inviteRecordRepository.findById(recordId);
        if (recordOpt.isEmpty() || recordOpt.get().getRewardGranted()) {
            return false;
        }
        
        InviteRecord record = recordOpt.get();
        record.setRewardGranted(true);
        record.setRewardGrantedAt(LocalDateTime.now());
        inviteRecordRepository.save(record);
        
        // 这里可以调用奖励发放逻辑，比如积分、优惠券等
        // 示例伪代码：
        // rewardService.grantPoints(record.getInviterUserId(), 100); // 给邀请人100积分
        // rewardService.grantPoints(record.getInvitedUserId(), 50); // 给被邀请人50积分
        
        log.info("邀请奖励已发放: 记录ID {}", recordId);
        return true;
    }
    
    /**
     * 获取用户的邀请码列表
     */
    @Operation(summary = "获取用户邀请码", description = "获取指定用户的所有邀请码")
    public List<InviteCode> getUserInviteCodes(@Parameter(description = "用户ID") Long userId) {
        return inviteCodeRepository.findAll();
    }
    
    /**
     * 获取用户邀请统计
     */
    @Operation(summary = "获取用户邀请统计", description = "获取指定用户的邀请统计数据")
    public InviteStats getInviteStats(@Parameter(description = "用户ID") Long userId) {
        long totalCodes = inviteCodeRepository.countByUserId(userId);
        long unusedCodes = inviteCodeRepository.countUnusedCodesByUserId(userId);
        long invitedUsers = inviteRecordRepository.countByInvitedUserId(userId);
        long rewardsGranted = inviteRecordRepository.countRewardsGrantedByInviterUserId(userId);
        
        return new InviteStats(totalCodes, unusedCodes, invitedUsers, rewardsGranted);
    }
    
    /**
     * 邀请统计数据类
     */
    public static class InviteStats {
        private final long totalCodes;
        private final long unusedCodes;
        private final long invitedUsers;
        private final long rewardsGranted;
        
        public InviteStats(long totalCodes, long unusedCodes, long invitedUsers, long rewardsGranted) {
            this.totalCodes = totalCodes;
            this.unusedCodes = unusedCodes;
            this.invitedUsers = invitedUsers;
            this.rewardsGranted = rewardsGranted;
        }
        
        // Getters
        public long getTotalCodes() { return totalCodes; }
        public long getUnusedCodes() { return unusedCodes; }
        public long getInvitedUsers() { return invitedUsers; }
        public long getRewardsGranted() { return rewardsGranted; }
    }
}