package uk.ac.bbsrc.tgac.miso.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bbsrc.tgac.miso.core.data.InstrumentModel;
import uk.ac.bbsrc.tgac.miso.core.data.impl.SequencingContainerModel;
import uk.ac.bbsrc.tgac.miso.core.service.ContainerModelService;
import uk.ac.bbsrc.tgac.miso.persistence.SequencingContainerModelStore;

@Transactional(rollbackFor = Exception.class)
@Service
public class DefaultContainerModelService implements ContainerModelService {
  @Autowired
  private SequencingContainerModelStore containerModelDao;

  @Override
  public SequencingContainerModel get(long id) throws IOException {
    return containerModelDao.get(id);
  }

  @Override
  public SequencingContainerModel find(InstrumentModel platform, String search, int partitionCount) throws IOException {
    return containerModelDao.find(platform, search, partitionCount);
  }

  @Override
  public List<SequencingContainerModel> list() throws IOException {
    return containerModelDao.list();
  }
}
