package uk.ac.bbsrc.tgac.miso.webapp.integrationtest;

import static org.junit.Assert.*;
import static uk.ac.bbsrc.tgac.miso.core.util.LimsUtils.isStringEmptyOrNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.AbstractListPage;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.AbstractListPage.Columns;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.AbstractListPage.ListTarget;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.ListPage;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.ListTabbedPage;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.ListTabbedPage.Tabs;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.element.DataTable;

public class ListTablesIT extends AbstractIT {

  private static final Set<String> samplesColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS, Columns.SAMPLE_CLASS,
      Columns.SAMPLE_TYPE, Columns.QC_PASSED, Columns.LOCATION, Columns.CREATION_DATE, Columns.LAST_MODIFIED, Columns.WARNINGS);
  private static final Set<String> librariesColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS,
      Columns.SAMPLE_NAME, Columns.SAMPLE_ALIAS, Columns.QC_PASSED, Columns.INDEX, Columns.LOCATION, Columns.LAST_MODIFIED,
      Columns.WARNINGS);
  private static final Set<String> libraryAliquotsColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS, Columns.WARNINGS,
      Columns.LIBRARY_NAME, Columns.LIBRARY_ALIAS, Columns.PLATFORM, Columns.TARGETED_SEQUENCING, Columns.INDEX, Columns.VOLUME, Columns.CONCENTRATION,
      Columns.NG_USED, Columns.VOLUME_USED, Columns.MATRIX_BARCODE, Columns.CREATOR, Columns.CREATION_DATE);
  private static final Set<String> poolsColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS,
      Columns.DESCRIPTION, Columns.DATE_CREATED, Columns.LIBRARY_ALIQUOTS, Columns.CONCENTRATION, Columns.LOCATION, Columns.AVG_INSERT_SIZE,
      Columns.LAST_MODIFIED);
  private static final Set<String> ordersColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS, Columns.PURPOSE,
      Columns.ORDER_DESCRIPTION, Columns.POOL_DESCRIPTION, Columns.INSTRUMENT_MODEL, Columns.LONGEST_INDEX, Columns.SEQUENCING_PARAMETERS,
      Columns.REMAINING, Columns.LAST_MODIFIED);
  private static final Set<String> containersColumns = Sets.newHashSet(Columns.SORT, Columns.ID, Columns.SERIAL_NUMBER,
      Columns.LAST_RUN_NAME, Columns.LAST_RUN_ALIAS, Columns.LAST_SEQUENCER, Columns.LAST_MODIFIED);
  private static final Set<String> runsColumns = Sets.newHashSet(Columns.NAME, Columns.ALIAS, Columns.SEQ_PARAMS, Columns.STATUS,
      Columns.START_DATE, Columns.END_DATE, Columns.LAST_MODIFIED);
  private static final Set<String> boxesColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS, Columns.DESCRIPTION,
      Columns.STORAGE_LOCATION, Columns.LOCATION, Columns.ITEMS_CAPACITY, Columns.SIZE);
  private static final Set<String> sequencersColumns = Sets.newHashSet(Columns.INSTRUMENT_NAME, Columns.PLATFORM, Columns.INSTRUMENT_MODEL,
      Columns.STATUS, Columns.SERIAL_NUMBER);
  private static final Set<String> kitsColumns = Sets.newHashSet(Columns.KIT_NAME, Columns.VERSION, Columns.MANUFACTURER,
      Columns.PART_NUMBER, Columns.STOCK_LEVEL, Columns.PLATFORM);
  private static final Set<String> indicesColumns = Sets.newHashSet(Columns.FAMILY, Columns.INDEX_NAME, Columns.SEQUENCE, Columns.POSITION);
  private static final Set<String> studiesColumns = Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ALIAS, Columns.DESCRIPTION,
      Columns.TYPE);
  private static final Set<String> printersColumns = Sets.newHashSet(Columns.SORT, Columns.PRINTER, Columns.DRIVER, Columns.LAYOUT, Columns.BACKEND,
      Columns.AVAILABLE);
  private static final Set<String> projectsColumns = Sets.newHashSet(Columns.NAME, Columns.ALIAS, Columns.SHORT_NAME,
      Columns.DESCRIPTION, Columns.PROGRESS);
  private static final Set<String> arraysColumns = Sets.newHashSet(Columns.ID, Columns.ALIAS, Columns.SERIAL_NUMBER, Columns.LAST_MODIFIED);
  private static final Set<String> arrayRunsColumns = Sets.newHashSet(Columns.ID, Columns.ALIAS, Columns.STATUS, Columns.START_DATE,
      Columns.END_DATE, Columns.LAST_MODIFIED);
  private static final Set<String> worksetsColumns = Sets.newHashSet(Columns.SORT, Columns.ID, Columns.ALIAS, Columns.ITEMS,
      Columns.DESCRIPTION, Columns.LAST_MODIFIED);
  private static final Set<String> storageLocationsColumns = Sets.newHashSet(Columns.FREEZER_NAME, Columns.IDENTIFICATION_BARCODE,
      Columns.MAP);

  private static final Set<String> poolsTabs = Sets.newHashSet(Tabs.ILLUMINA, Tabs.PACBIO);
  private static final Set<String> ordersTabs = Sets.newHashSet(Tabs.ILLUMINA, Tabs.PACBIO);
  private static final Set<String> containersTabs = Sets.newHashSet(Tabs.ILLUMINA, Tabs.PACBIO);
  private static final Set<String> runsTabs = Sets.newHashSet(Tabs.ILLUMINA, Tabs.PACBIO);
  private static final Set<String> boxesTabs = Sets.newHashSet(Tabs.DNA, Tabs.LIBRARIES, Tabs.RNA, Tabs.SEQUENCING, Tabs.STORAGE,
      Tabs.TISSUE);
  private static final Set<String> kitsTabs = Sets.newHashSet(Tabs.CLUSTERING, Tabs.EXTRACTION, Tabs.LIBRARY, Tabs.MULTIPLEXING,
      Tabs.SEQUENCING);
  private static final Set<String> indicesTabs = Sets.newHashSet(Tabs.ILLUMINA, Tabs.PACBIO);
  private static final Set<String> worksetsTabs = Sets.newHashSet(Tabs.MINE, Tabs.ALL);
  private static final Set<String> storageLocationTabs = Sets.newHashSet(Tabs.FREEZERS, Tabs.ROOMS);

  private static final Map<String, Set<String>> tabsForTarget;
  static {
    Map<String, Set<String>> tabs = new HashMap<>();
    tabs.put(ListTarget.POOLS, poolsTabs);
    tabs.put(ListTarget.ORDERS_OUTSTANDING, ordersTabs);
    tabs.put(ListTarget.ORDERS_ALL, ordersTabs);
    tabs.put(ListTarget.ORDERS_IN_PROGRESS, ordersTabs);
    tabs.put(ListTarget.CONTAINERS, containersTabs);
    tabs.put(ListTarget.RUNS, runsTabs);
    tabs.put(ListTarget.BOXES, boxesTabs);
    tabs.put(ListTarget.KITS, kitsTabs);
    tabs.put(ListTarget.INDICES, indicesTabs);
    tabs.put(ListTarget.WORKSETS, worksetsTabs);
    tabs.put(ListTarget.STORAGE_LOCATIONS, storageLocationTabs);
    tabs.put(ListTarget.POOL_ORDERS, Sets.newHashSet(Tabs.OUTSTANDING, Tabs.FULFILLED, Tabs.DRAFT));
    tabsForTarget = Collections.unmodifiableMap(tabs);
  }

  private static final Set<String> completionHeaders = Sets.newHashSet(Columns.COMPLETED, Columns.REQUESTED,
      Columns.RUNNING, Columns.FAILED, Columns.STARTED, Columns.STOPPED, Columns.UNKNOWN);

  // some tabs have no data, so we want to ensure we do all sort tests on tabs with data
  private static final Map<String, String> sortOnTab;
  static {
    Map<String, String> preferredTab = new HashMap<>();
    preferredTab.put(ListTarget.POOLS, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.ORDERS_OUTSTANDING, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.ORDERS_ALL, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.ORDERS_IN_PROGRESS, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.CONTAINERS, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.RUNS, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.BOXES, Tabs.STORAGE);
    preferredTab.put(ListTarget.KITS, Tabs.LIBRARY);
    preferredTab.put(ListTarget.INDICES, Tabs.ILLUMINA);
    preferredTab.put(ListTarget.WORKSETS, Tabs.MINE);
    preferredTab.put(ListTarget.POOL_ORDERS, Tabs.OUTSTANDING);
    sortOnTab = Collections.unmodifiableMap(preferredTab);
  }

  private static final Comparator<String> standardComparator = (s1, s2) -> s1.compareToIgnoreCase(s2);

  /**
   * Comparator for columns which render the boolean values as symbols.
   */
  private static final Comparator<String> booleanColumnComparator = (qcPassed1, qcPassed2) -> {
    return Integer.compare(getBooleanValue(qcPassed1), getBooleanValue(qcPassed2));
  };

  private static int getBooleanValue(String symbol) {
    switch (symbol) {
    case "?":
      return -1;
    case "✔":
      return 0;
    case "✘":
      return 1;
    default:
      throw new IllegalArgumentException("Invalid QC Passed symbol");
    }
  }

  private static final String NAME_REGEX = "^[A-Z]{3}\\d+$";
  /**
   * Compares names with the same prefix by number (e.g. SAM8 and SAM10 compare as 8 and 10, ignoring the 'SAM').
   * If the names don't match the entity name pattern, they are compared regularly as Strings
   */
  private static final Comparator<String> nameNumericComparator = (name1, name2) -> {
    if (name1.matches(NAME_REGEX) && name2.matches(NAME_REGEX) && name1.substring(0, 3).equals(name2.substring(0, 3))) {
      int id1 = Integer.parseInt(name1.substring(3, name1.length()));
      int id2 = Integer.parseInt(name2.substring(3, name2.length()));
      return Integer.compare(id1, id2);
    } else {
      return standardComparator.compare(name1, name2);
    }
  };

  private static final Comparator<String> numericComparator = (num1, num2) -> {
    if (NumberUtils.isCreatable(num1) && NumberUtils.isCreatable(num2)) {
      double d1 = Double.valueOf(num1);
      double d2 = Double.valueOf(num2);
      return Double.compare(d1, d2);
    } else {
      return standardComparator.compare(num1, num2);
    }
  };

  private static final Comparator<String> numericIgnoreUnitsComparator = (num1, num2) -> {
    return numericComparator.compare(removeUnits(num1), removeUnits(num2));
  };

  private static final Pattern numberWithUnits = Pattern.compile("^(-?\\d+(?:\\.\\d+)?)(?: .+)?$");

  private static String removeUnits(String num) {
    Matcher m1 = numberWithUnits.matcher(num);
    if (!m1.matches()) {
      throw new IllegalArgumentException("Input does not match expected pattern: " + num);
    }
    return m1.group(1);
  }

  @Before
  public void setup() {
    loginAdmin();
  }

  private ListPage getList(String listTarget) {
    return ListPage.getListPage(getDriver(), getBaseUrl(), listTarget);
  }

  private ListTabbedPage getTabbedList(String listTarget) {
    return ListTabbedPage.getTabbedListPage(getDriver(), getBaseUrl(), listTarget);
  }

  @Test
  public void testIndexDistanceToolSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    ListPage page = ListPage.getListPage(getDriver(), getBaseUrl(), "tools/indexdistance");
    DataTable table = page.getTable();
    Set<String> expected = indicesColumns;
    expected.add(Columns.SORT); // Checkbox column
    expected.add(Columns.PLATFORM); // Platform column
    List<String> headings = table.getColumnHeadings();
    assertEquals("number of columns", expected.size(), headings.size());
    for (String col : indicesColumns) {
      assertTrue("Check for column: '" + col + "'", headings.contains(col));
    }
  }

  @Test
  public void testIdentitySearchToolSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    ListPage page = ListPage.getListPage(getDriver(), getBaseUrl(), "tools/identitysearch");
    DataTable table = page.getTable();
    List<String> headings = table.getColumnHeadings();
    assertEquals("number of columns", samplesColumns.size(), headings.size());
    for (String col : samplesColumns) {
      assertTrue("Check for column: '" + col + "'", headings.contains(col));
    }
  }

  @Test
  public void testListSamplesPageSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    testPageSetup(ListTarget.SAMPLES, samplesColumns);
  }

  @Test
  public void testListSamplesColumnSort() throws Exception {
    // Goal: ensure all sortable columns can be sorted without errors
    testColumnsSort(ListTarget.SAMPLES);
  }

  @Test
  public void testListLibrariesPageSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    testPageSetup(ListTarget.LIBRARIES, librariesColumns);
  }

  @Test
  public void testListLibrariesColumnSort() throws Exception {
    // Goal: ensure all sortable columns can be sorted without errors
    testColumnsSort(ListTarget.LIBRARIES);
  }

  @Test
  public void testListLibrariesWarnings() throws Exception {
    testWarningNormal(ListTarget.LIBRARIES, "LIB901", "Negative Volume", Columns.WARNINGS);
  }

  @Test
  public void testListLibraryAliquotsPageSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    testPageSetup(ListTarget.LIBRARY_ALIQUOTS, libraryAliquotsColumns);
  }

  @Test
  public void testListLibraryAliquotsColumnSort() throws Exception {
    // Goal: ensure all sortable columns can be sorted without errors
    testColumnsSort(ListTarget.LIBRARY_ALIQUOTS);
  }

  @Test
  public void testListPoolsPageSetup() throws Exception {
    // Goal: ensure all expected columns are present and no extra
    testTabbedPageSetup(ListTarget.POOLS, poolsColumns);
  }

  @Test
  public void testListPoolsColumnSort() throws Exception {
    // Goal: ensure all sortable columns can be sorted without errors
    testTabbedColumnsSort(ListTarget.POOLS);
  }

  @Test
  public void testListPoolsWarnings() throws Exception {
    testWarningTabbed(ListTarget.POOLS, "no indices", "MISSING INDEX", Columns.DESCRIPTION);
    testWarningTabbed(ListTarget.POOLS, "similar index", "Near-Duplicate Indices", Columns.DESCRIPTION);
    testWarningTabbed(ListTarget.POOLS, "same index", "DUPLICATE INDICES", Columns.DESCRIPTION);
    testWarningTabbed(ListTarget.POOLS, "low quality library", "Low Quality Libraries", Columns.DESCRIPTION);
  }

  @Test
  public void testListOrdersSetup() throws Exception {
    for (String pageName : new String[] { ListTarget.ORDERS_ALL,  ListTarget.ORDERS_OUTSTANDING,  ListTarget.ORDERS_IN_PROGRESS }) {
      // this one is special because the number of order completion states is variable
      ListTabbedPage page = getTabbedList(pageName);
      DataTable table = page.getTable();
      List<String> headings = table.getColumnHeadings();
      // size = order columns + some number of completion state columns
      assertTrue(ordersColumns.size() <= headings.size());
      for (String col : ordersColumns) {
        assertTrue("Check for column: '" + col + "'", headings.contains(col));
      }
      headings.removeAll(ordersColumns);

      // confirm that order completion columns are part of the expected set and are not duplicated
      Set<String> foundCompletionHeaders = new HashSet<>();
      for (String remaining : headings) {
        if (!completionHeaders.contains(remaining)) throw new IllegalArgumentException("Found unexpected column '" + remaining + "' on " + pageName);
        if (!foundCompletionHeaders.add(remaining))
          throw new IllegalArgumentException("Found duplicate completion column '" + foundCompletionHeaders + "' on " + pageName);
      }
    }
  }

  @Test
  public void testListOrdersColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.ORDERS_ALL);
    testTabbedColumnsSort(ListTarget.ORDERS_OUTSTANDING);
    testTabbedColumnsSort(ListTarget.ORDERS_IN_PROGRESS);
  }

  @Test
  public void testListOrdersWarnings() throws Exception {
    testWarningTabbed(ListTarget.ORDERS_ALL, "no indices", "MISSING INDEX", Columns.POOL_DESCRIPTION);
    testWarningTabbed(ListTarget.ORDERS_ALL, "similar index", "Near-Duplicate Indices", Columns.POOL_DESCRIPTION);
    testWarningTabbed(ListTarget.ORDERS_ALL, "same index", "DUPLICATE INDICES", Columns.POOL_DESCRIPTION);
    testWarningTabbed(ListTarget.ORDERS_ALL, "low quality library", "Low Quality Libraries", Columns.POOL_DESCRIPTION);
  }

  @Test
  public void testListContainersSetup() throws Exception {
    testTabbedPageSetup(ListTarget.CONTAINERS, containersColumns);
  }

  @Test
  public void testListContainersColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.CONTAINERS);
  }

  @Test
  public void testListRunsSetup() throws Exception {
    testTabbedPageSetup(ListTarget.RUNS, runsColumns);
  }

  @Test
  public void testListRunsColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.RUNS);
  }

  @Test
  public void testListBoxesSetup() throws Exception {
    testTabbedPageSetup(ListTarget.BOXES, boxesColumns);
  }

  @Test
  public void testListBoxesColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.BOXES);
  }

  @Test
  public void testListSequencersSetup() throws Exception {
    testPageSetup(ListTarget.INSTRUMENTS, sequencersColumns);
  }

  @Test
  public void testListSequencersColumnSort() throws Exception {
    testColumnsSort(ListTarget.INSTRUMENTS);
  }

  @Test
  public void testListKitsSetup() throws Exception {
    testTabbedPageSetup(ListTarget.KITS, kitsColumns);
  }

  @Test
  public void testListKitsColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.KITS);
  }

  @Test
  public void testListIndicesSetup() throws Exception {
    testTabbedPageSetup(ListTarget.INDICES, indicesColumns);
  }

  @Test
  public void testListIndicesColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.INDICES);
  }

  @Test
  public void testListStudiesSetup() throws Exception {
    testPageSetup(ListTarget.STUDIES, studiesColumns);
  }

  @Test
  public void testListStudiesColumnSort() throws Exception {
    testColumnsSort(ListTarget.STUDIES);
  }

  @Test
  public void testListPrintersSetup() throws Exception {
    testPageSetup(ListTarget.PRINTERS, printersColumns);
  }

  @Test
  public void testListPrintersColumnSort() throws Exception {
    testColumnsSort(ListTarget.PRINTERS);
  }

  @Test
  public void testListProjectsSetup() throws Exception {
    testPageSetup(ListTarget.PROJECTS, projectsColumns);
  }

  @Test
  public void testListProjectsColumnSort() throws Exception {
    testColumnsSort(ListTarget.PROJECTS);
  }

  @Test
  public void testListArraysSetup() throws Exception {
    testPageSetup(ListTarget.ARRAYS, arraysColumns);
  }

  @Test
  public void testListArraysColumnSort() throws Exception {
    testColumnsSort(ListTarget.ARRAYS);
  }

  @Test
  public void testListArrayModelsSetup() throws Exception {
    testPageSetup(ListTarget.ARRAY_MODELS, Sets.newHashSet(Columns.SORT, Columns.ALIAS, Columns.ROWS, Columns.COLUMNS));
  }

  @Test
  public void testListArrayModelsColumnSort() throws Exception {
    testColumnsSort(ListTarget.ARRAY_MODELS);
  }

  @Test
  public void testListArrayRunsSetup() throws Exception {
    testPageSetup(ListTarget.ARRAY_RUNS, arrayRunsColumns);
  }

  @Test
  public void testListArrayRunsColumnSort() throws Exception {
    testColumnsSort(ListTarget.ARRAY_RUNS);
  }

  @Test
  public void testListWorksetsSetup() throws Exception {
    testTabbedPageSetup(ListTarget.WORKSETS, worksetsColumns);
  }

  @Test
  public void testListWorksetsColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.WORKSETS);
  }

  @Test
  public void testListTissueTypesSetup() throws Exception {
    testPageSetup(ListTarget.TISSUE_TYPES, Sets.newHashSet(Columns.SORT, Columns.ALIAS, Columns.DESCRIPTION));
  }

  @Test
  public void testListTissueTypesColumnSort() throws Exception {
    testColumnsSort(ListTarget.TISSUE_TYPES);
  }

  @Test
  public void testListReferenceGenomesSetup() throws Exception {
    testPageSetup(ListTarget.REFERENCE_GENOMES,
        Sets.newHashSet(Columns.SORT, Columns.ALIAS, Columns.DEFAULT_SCI_NAME));
  }

  @Test
  public void testListReferenceGenomesColumnSort() throws Exception {
    testColumnsSort(ListTarget.REFERENCE_GENOMES);
  }

  @Test
  public void testListSampleTypesSetup() throws Exception {
    testPageSetup(ListTarget.SAMPLE_TYPES, Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.ARCHIVED));
  }

  @Test
  public void testListSampleTypesColumnSort() throws Exception {
    testColumnsSort(ListTarget.SAMPLE_TYPES);
  }

  @Test
  public void testListStainsSetup() throws Exception {
    testPageSetup(ListTarget.STAINS, Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.CATEGORY));
  }

  @Test
  public void testListStainsColumnSort() throws Exception {
    testColumnsSort(ListTarget.STAINS);
  }

  @Test
  public void testListStainCategoriesSetup() throws Exception {
    testPageSetup(ListTarget.STAIN_CATEGORIES, Sets.newHashSet(Columns.SORT, Columns.NAME));
  }

  @Test
  public void testListStainCategoriesColumnSort() throws Exception {
    testColumnsSort(ListTarget.STAIN_CATEGORIES);
  }

  @Test
  public void testListDetailedQcStatusSetup() throws Exception {
    testPageSetup(ListTarget.DETAILED_QC_STATUS,
        Sets.newHashSet(Columns.SORT, Columns.DESCRIPTION, Columns.QC_PASSED, Columns.NOTE_REQUIRED));
  }

  @Test
  public void testListDetailedQcStatusColumnSort() throws Exception {
    testColumnsSort(ListTarget.DETAILED_QC_STATUS);
  }

  @Test
  public void testListBoxSizesSetup() throws Exception {
    testPageSetup(ListTarget.BOX_SIZES, Sets.newHashSet(Columns.SORT, Columns.ROWS, Columns.COLUMNS, Columns.SCANNABLE));
  }

  @Test
  public void testListBoxSizesColumnSort() throws Exception {
    testColumnsSort(ListTarget.BOX_SIZES);
  }

  @Test
  public void testListBoxUsesSetup() throws Exception {
    testPageSetup(ListTarget.BOX_USES, Sets.newHashSet(Columns.SORT, Columns.ALIAS));
  }

  @Test
  public void testListBoxUsesColumnSort() throws Exception {
    testColumnsSort(ListTarget.BOX_USES);
  }

  @Test
  public void testListPartitionQcTypeSetup() throws Exception {
    testPageSetup(ListTarget.PARTITION_QC_TYPE,
        Sets.newHashSet(Columns.SORT, Columns.DESCRIPTION, Columns.NOTE_REQUIRED, Columns.ORDER_FULFILLED, Columns.ANALYSIS_SKIPPED));
  }

  @Test
  public void testListPartitionQcTypeColumnSort() throws Exception {
    testColumnsSort(ListTarget.PARTITION_QC_TYPE);
  }

  @Test
  public void testListStudyTypesSetup() throws Exception {
    testPageSetup(ListTarget.STUDY_TYPES, Sets.newHashSet(Columns.SORT, Columns.NAME));
  }

  @Test
  public void testListStudyTypesColumnSort() throws Exception {
    testColumnsSort(ListTarget.STUDY_TYPES);
  }

  @Test
  public void testListLocationMapsSetup() throws Exception {
    testPageSetup(ListTarget.LOCATION_MAPS, Sets.newHashSet(Columns.SORT, Columns.FILENAME, Columns.DESCRIPTION));
  }

  @Test
  public void testListLocationMapsColumnSort() throws Exception {
    testColumnsSort(ListTarget.LOCATION_MAPS);
  }

  @Test
  public void testListLibrarySelectionSetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_SELECTION_TYPES, Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.DESCRIPTION));
  }

  @Test
  public void testListLibrarySelectionColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_SELECTION_TYPES);
  }

  @Test
  public void testListLibraryStrategySetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_STRATEGY_TYPES, Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.DESCRIPTION));
  }

  @Test
  public void testListLibraryStrategyColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_STRATEGY_TYPES);
  }

  @Test
  public void testListLibrarySpikeInsSetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_SPIKE_INS, Sets.newHashSet(Columns.SORT, Columns.ALIAS));
  }

  @Test
  public void testListLibrarySpikeInsColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_SPIKE_INS);
  }

  @Test
  public void testListLibraryDesignCodesSetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_DESIGN_CODES,
        Sets.newHashSet(Columns.SORT, Columns.CODE, Columns.DESCRIPTION, Columns.TARGETED_SEQUENCING_REQD));
  }

  @Test
  public void testListLibraryDesignCodesColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_DESIGN_CODES);
  }

  @Test
  public void testListLibraryDesignsSetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_DESIGNS,
        Sets.newHashSet(Columns.SORT, Columns.NAME, Columns.SAMPLE_CLASS, Columns.LIBRARY_SELECTION, Columns.LIBRARY_STRATEGY,
            Columns.LIBRARY_DESIGN_CODE));
  }

  @Test
  public void testListLibraryDesignsColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_DESIGNS);
  }

  @Test
  public void testListLibraryTypesSetup() throws Exception {
    testPageSetup(ListTarget.LIBRARY_TYPES,
        Sets.newHashSet(Columns.SORT, Columns.DESCRIPTION, Columns.PLATFORM, Columns.ABBREVIATION, Columns.ARCHIVED));
  }

  @Test
  public void testListLibraryTypesColumnSort() throws Exception {
    testColumnsSort(ListTarget.LIBRARY_TYPES);
  }

  @Test
  public void testListTabbedStorageLocationsSetup() throws Exception {
    testTabbedPageSetup(ListTarget.STORAGE_LOCATIONS, storageLocationsColumns);
  }

  @Test
  public void testListStorageLocationsColumnSort() throws Exception {
    testColumnsSort(ListTarget.STORAGE_LOCATIONS);
  }

  @Test
  public void testListOrderPurposesSetup() throws Exception {
    testPageSetup(ListTarget.ORDER_PURPOSES,
        Sets.newHashSet(Columns.SORT, Columns.ALIAS));
  }

  @Test
  public void testListOrderPurposesColumnSort() throws Exception {
    testColumnsSort(ListTarget.ORDER_PURPOSES);
  }

  @Test
  public void testListPoolOrdersSetup() throws Exception {
    testTabbedPageSetup(ListTarget.POOL_ORDERS, Sets.newHashSet(Columns.SORT, Columns.ID, Columns.ALIAS, Columns.PURPOSE,
        Columns.DESCRIPTION, Columns.LIBRARY_ALIQUOTS, Columns.INSTRUMENT_MODEL, Columns.SEQUENCING_PARAMETERS, Columns.PARTITIONS));
  }

  @Test
  public void testListPoolOrdersColumnSort() throws Exception {
    testTabbedColumnsSort(ListTarget.POOL_ORDERS);
  }

  private void testPageSetup(String listTarget, Set<String> targetColumns) {
    // Goal: confirm that all expected columns are present
    ListPage page = getList(listTarget);
    DataTable table = page.getTable();
    List<String> headings = table.getColumnHeadings();
    assertEquals("number of columns", targetColumns.size(), headings.size());
    for (String col : targetColumns) {
      assertTrue("Check for column: '" + col + "'", headings.contains(col));
    }
  }

  private void testTabbedPageSetup(String listTarget, Set<String> targetColumns) {
    // Goal: confirm that all expected tabs and columns are present
    ListTabbedPage page = getTabbedList(listTarget);
    DataTable table = page.getTable();
    // confirm expected number of tabs
    Set<String> tabs = tabsForTarget.get(listTarget);
    Set<String> foundTabs = page.getTabHeadings();
    for (String tab : tabs) {
      assertTrue("Check for tab '" + tab + "': ", foundTabs.contains(tab));
    }

    List<String> headings = table.getColumnHeadings();
    assertEquals("number of columns", targetColumns.size(), headings.size());
    for (String col : targetColumns) {
      assertTrue("Check for column: '" + col + "'", headings.contains(col));
    }
  }

  private void testColumnsSort(String listTarget) {
    // confirm that sortable columns can be sorted on
    ListPage page = getList(listTarget);
    sortColumns(page.getTable(), page);
  }

  private void testTabbedColumnsSort(String listTarget) {
    // confirm that sortable columns can be sorted on
    // note that this sorts in a single tab only, as different tabs should not have different columns.
    ListTabbedPage page = getTabbedList(listTarget);
    DataTable table = page.getTable();
    sortColumns(table, page);

    page.clickTab(sortOnTab.get(listTarget));
    Set<String> tabHeadings = page.getTabHeadings();
    tabHeadings.forEach(tabHeading -> {
      page.clickTab(tabHeading);
      assertTrue("clicked tab without errors", isStringEmptyOrNull(page.getErrors().getText()));
    });
  }

  private void sortColumns(DataTable table, AbstractListPage page) {
    List<String> headings = table.getSortableColumnHeadings();
    for (String heading : headings) {
      // sort one way
      table.sortByColumn(heading);
      assertTrue("first sort on column '" + heading, isStringEmptyOrNull(page.getErrors().getText()));
      // if there are at least two rows, ensure that sort was correct
      if (!table.isTableEmpty() && table.countRows() > 1) {
        int sort1 = compareFirstTwoNonMatchingValues(table, heading);
        List<String> columnSort1 = getColumn(table, heading);
        // sort the other way
        table.sortByColumn(heading);
        assertTrue("second sort on column '" + heading, isStringEmptyOrNull(page.getErrors().getText()));
        int sort2 = compareFirstTwoNonMatchingValues(table, heading);
        List<String> columnSort2 = getColumn(table, heading);

        // compare results (if either is 0, value of the other can be anything though)
        if (sort1 != 0) {
          assertTrue(
              heading + " column second sort order should differ from first:"
                  + " First sort order was <" + String.join(", ", columnSort1) + ">, "
                  + "Second sort order was <" + String.join(", ", columnSort2) + ">",
              sort2 == 0 || sort1 > 0 != sort2 > 0);
        }
      }
    }
  }

  private List<String> getColumn(DataTable table, String heading) {
    List<String> colVals = new ArrayList<>();
    for (int rowNum = 0; rowNum < table.countRows(); rowNum++) {
      colVals.add(table.getTextAtCell(heading, rowNum));
    }
    return colVals;
  }

  private void testWarningNormal(String target, String query, String warning, String column) {
    ListPage page = getList(target);
    DataTable table = page.getTable();
    table.searchFor(query);
    assertTrue(String.format("'%s' column does not contain '%s' warning", column, warning),
        table.doesColumnContainSubstring(column, warning));
  }

  private void testWarningTabbed(String target, String query, String warning, String column) {
    ListTabbedPage page = getTabbedList(target);
    DataTable table = page.getTable();
    table.searchFor(query);
    assertTrue(String.format("'%s' column does not contain '%s' warning", column, warning),
        table.doesColumnContainSubstring(column, warning));
  }

  private int compareFirstTwoNonMatchingValues(DataTable table, String heading) {
    String row1Val = table.getTextAtCell(heading, 0);
    String row2Val = table.getTextAtCell(heading, 1);
    for (int rowNum = 2; row1Val.equals(row2Val) && rowNum < table.countRows(); rowNum++) {
      row1Val = row2Val;
      row2Val = table.getTextAtCell(heading, rowNum);
    }
    Comparator<String> columnComparator = getComparator(heading);
    return columnComparator.compare(row1Val, row2Val);
  }

  private static Comparator<String> getComparator(String column) {
    switch (column) {
    case Columns.QC_PASSED:
    case Columns.NOTE_REQUIRED:
    case Columns.ORDER_FULFILLED:
    case Columns.ANALYSIS_SKIPPED:
      return booleanColumnComparator;
    case Columns.LIBRARY_NAME:
    case Columns.NAME:
    case Columns.SAMPLE_NAME:
      return nameNumericComparator;
    case Columns.CONCENTRATION:
      return numericIgnoreUnitsComparator;
    default:
      return standardComparator;
    }
  }

}
