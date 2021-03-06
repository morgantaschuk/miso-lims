package uk.ac.bbsrc.tgac.miso.persistence;

import java.io.IOException;
import java.util.Collection;

import uk.ac.bbsrc.tgac.miso.core.data.type.QcType;

public interface QualityControlTypeStore {
  Collection<QcType> list() throws IOException;

  QcType get(long id) throws IOException;

  long create(QcType qcType) throws IOException;

  void update(QcType qcType) throws IOException;
}
