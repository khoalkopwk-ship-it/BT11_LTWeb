package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder
                // Context bên dưới cho phép forward an toàn trên Tomcat 11.
                .setDispatchMode(DispatchMode.FORWARD)
                // SiteMesh 3 mặc định thêm prefix /WEB-INF/decorators/.
                .addDecoratorPath("/admin", "admin.jsp")
                .addDecoratorPath("/admin/*", "admin.jsp")
                .addDecoratorPath("/*", "user.jsp")
                .addExcludedPath("/WEB-INF/*")
                .addExcludedPath("/assets/*");
    }
}
