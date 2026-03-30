package com.snail.admin.aspect;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.entity.UserRole;
import com.snail.util.IAuthenticationFacade;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class AdminPermissionAspect {
    
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    
    @Before("@annotation(requireAdmin)")
    public void checkAdminPermission(RequireAdmin requireAdmin) {
        // 获取当前认证用户
        var authentication = authenticationFacade.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户未登录");
        }
        
        // 检查用户角色是否符合要求
        String[] requiredRoles = requireAdmin.roles();
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof com.snail.entity.User) {
            com.snail.entity.User user = (com.snail.entity.User) principal;
            UserRole userRole = user.getRole();
            
            boolean hasPermission = false;
            for (String requiredRole : requiredRoles) {
                if (userRole.name().equals(requiredRole)) {
                    hasPermission = true;
                    break;
                }
            }
            
            if (!hasPermission) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足，仅管理员可访问");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "权限验证失败");
        }
    }
}