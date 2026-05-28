package com.example.edutops.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình OpenAPI (Swagger UI) tích hợp xác thực JWT Bearer Token.
 * Giúp lập trình viên có thể trực tiếp nhập Token JWT để thử nghiệm các REST APIs yêu cầu phân quyền.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("EduTopS REST APIs Specification")
                        .version("1.0")
                        .description("Tài liệu đặc tả toàn bộ REST APIs của hệ thống quản lý vận hành trung tâm giáo dục EduTopS."))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập mã Bearer JWT Token của bạn (không bao gồm chữ 'Bearer ')")));
    }
}
