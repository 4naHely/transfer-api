package com.ana.transferapi;

import com.ana.transferapi.repository.ClientAccountRepository;
import com.ana.transferapi.repository.TransfersRepository;
import com.ana.transferapi.repository.entity.ClientAccountEntity;
import com.ana.transferapi.repository.entity.TransfersEntity;
import java.math.BigDecimal;
import java.util.function.Function;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ClientAccountEntity.class, TransfersEntity.class},
    loader = AnnotationConfigContextLoader.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TransferApiApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractItSupport {

  @Autowired protected ClientAccountRepository clientAccountRepository;

  @Autowired protected TransfersRepository transferHistoricRepository;

  public ClientAccountEntity buildAndSaveClientAccountEntity(
      Function<
              ClientAccountEntity.ClientAccountEntityBuilder,
              ClientAccountEntity.ClientAccountEntityBuilder>
          map) {
    ClientAccountEntity.ClientAccountEntityBuilder builder =
        ClientAccountEntity.builder().name("test").balance(new BigDecimal("10.00"));

    return clientAccountRepository.saveAndFlush(map.apply(builder).build());
  }
}
