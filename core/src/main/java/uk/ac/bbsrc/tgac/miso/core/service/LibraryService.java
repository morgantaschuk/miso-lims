package uk.ac.bbsrc.tgac.miso.core.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import uk.ac.bbsrc.tgac.miso.core.data.Barcodable.EntityType;
import uk.ac.bbsrc.tgac.miso.core.data.Library;
import uk.ac.bbsrc.tgac.miso.core.util.PaginatedDataSource;

public interface LibraryService
    extends PaginatedDataSource<Library>, BarcodableService<Library>, DeleterService<Library>, NoteService<Library>, SaveService<Library> {

  @Override
  default EntityType getEntityType() {
    return EntityType.LIBRARY;
  }

  int count() throws IOException;

  long countBySearch(String querystr) throws IOException;

  List<Library> list() throws IOException;

  Library getAdjacentLibrary(long libraryId, boolean before) throws IOException;

  Library getByBarcode(String barcode) throws IOException;

  List<Library> listByBarcodeList(List<String> barcodeList) throws IOException;

  List<Library> listByIdList(List<Long> idList) throws IOException;

  Library getByPositionId(long positionId) throws IOException;

  List<Library> listBySearch(String querystr) throws IOException;

  List<Library> listByAlias(String alias) throws IOException;

  List<Library> searchByCreationDate(Date from, Date to) throws IOException;

  List<Library> listBySampleId(long sampleId) throws IOException;

  List<Library> listByProjectId(long projectId) throws IOException;

}
