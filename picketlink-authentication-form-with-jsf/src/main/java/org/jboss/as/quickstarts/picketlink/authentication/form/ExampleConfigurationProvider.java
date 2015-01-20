package org.jboss.as.quickstarts.picketlink.authentication.form;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

@RewriteConfiguration
public class ExampleConfigurationProvider extends HttpConfigurationProvider
{
    @Override
    public int priority()
    {
        return 10;
    }

    @Override
    public Configuration getConfiguration(final ServletContext context)
    {
        return ConfigurationBuilder.begin()
                    .addRule(Join.path("/").to("/home.jsf").withInboundCorrection())
                    .addRule(Join.path("/login").to("/login.jsf").withInboundCorrection())
                    .addRule(Join.path("/error").to("/error.jsf").withInboundCorrection());
    }
}