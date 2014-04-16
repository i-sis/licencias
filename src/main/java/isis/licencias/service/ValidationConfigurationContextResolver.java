package isis.licencias.service;

/*
 * Integrating Bean Validation with JAX-RS in Java EE 7
 * https://github.com/samaxes/jaxrs-beanvalidation-javaee7
 *
 * Copyright (c) 2013 samaxes.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.validation.BootstrapConfiguration;
import javax.validation.Configuration;
import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.validation.GeneralValidatorImpl;
import org.jboss.resteasy.spi.validation.GeneralValidator;

/**
 * Custom configuration of validation. This configuration can define custom:
 * <ul>
 * <li>MessageInterpolator - interpolates a given constraint violation message.</li>
 * <li>TraversableResolver - determines if a property can be accessed by the Bean Validation provider.</li>
 * <li>ConstraintValidatorFactory - instantiates a ConstraintValidator instance based off its class.
 * <li>ParameterNameProvider - provides names for method and constructor parameters.</li> *
 * </ul>
 */
@Provider
public class ValidationConfigurationContextResolver implements ContextResolver<GeneralValidator> {

    /**
     * Get a context of type {@code GeneralValidator} that is applicable to the supplied type.
     *
     * @param type the class of object for which a context is desired
     * @return a context for the supplied type or {@code null} if a context for the supplied type is not available from
     *         this provider.
     */
    @Override
    public GeneralValidator getContext(Class<?> type) {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        BootstrapConfiguration bootstrapConfiguration = config.getBootstrapConfiguration();

        config.messageInterpolator(new LocaleSpecificMessageInterpolator(Validation.byDefaultProvider().configure()
                .getDefaultMessageInterpolator()));
        config.parameterNameProvider(new CustomParameterNameProvider());

        return new GeneralValidatorImpl(config.buildValidatorFactory(),
        								bootstrapConfiguration.isExecutableValidationEnabled(),
        								bootstrapConfiguration.getDefaultValidatedExecutableTypes());
    }

    /**
     * If method input parameters are invalid, this class returns actual parameter names instead of the default ones (
     * {@code arg0, arg1, ...})
     */
    private class CustomParameterNameProvider implements ParameterNameProvider {

        private final ParameterNameProvider nameProvider;

        public CustomParameterNameProvider() {
            nameProvider = Validation.byDefaultProvider().configure().getDefaultParameterNameProvider();
        }

        @Override
        public List<String> getParameterNames(final Constructor<?> constructor) {
            return nameProvider.getParameterNames(constructor);
        }

        
        @Override
        public List<String> getParameterNames(final Method method) {
            if ("getLicencia".equals(method.getName())) {
                return Arrays.asList("CN","dni","title","OU","O","email","ST","C");
            }
            return nameProvider.getParameterNames(method);
        }
    }
}