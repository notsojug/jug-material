package jug.swarm.swarmdemo.cdi.config;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

public class EagerExtension implements Extension {
  private final List<Bean<?>> eagerBeansList = new ArrayList<>();

  public <T> void collect(@Observes final ProcessBean<T> event) {
    if (event.getAnnotated().isAnnotationPresent(Eager.class)
        && event.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {
      eagerBeansList.add(event.getBean());
    }
  }

  public void load(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
    for (final Bean<?> bean : eagerBeansList) {
      // note: toString() is important to instantiate the bean
      beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean))
          .toString();
    }
  }
}
