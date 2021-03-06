package uk.ac.bbsrc.tgac.miso.webapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.bbsrc.tgac.miso.core.data.type.LibraryStrategyType;
import uk.ac.bbsrc.tgac.miso.core.security.AuthorizationManager;
import uk.ac.bbsrc.tgac.miso.core.service.LibraryStrategyService;
import uk.ac.bbsrc.tgac.miso.core.service.ProviderService;
import uk.ac.bbsrc.tgac.miso.dto.Dtos;
import uk.ac.bbsrc.tgac.miso.dto.LibraryStrategyTypeDto;

@Controller
@RequestMapping("/librarystrategy")
public class LibraryStrategyController extends AbstractTypeDataController<LibraryStrategyType, LibraryStrategyTypeDto> {

  @Autowired
  private LibraryStrategyService libraryStrategyService;

  @Autowired
  private AuthorizationManager authorizationManager;

  public LibraryStrategyController() {
    super("Library Strategy Types", "librarystrategy", "librarystrategy");
  }

  @GetMapping("/list")
  public ModelAndView list(ModelMap model) throws IOException {
    return listStatic(libraryStrategyService.list(), model);
  }

  @GetMapping("/bulk/new")
  public ModelAndView create(@RequestParam("quantity") Integer quantity, ModelMap model) throws IOException {
    return bulkCreate(quantity, model);
  }

  @GetMapping("/bulk/edit")
  public ModelAndView edit(@RequestParam("ids") String idString, ModelMap model) throws IOException {
    return bulkEdit(idString, model);
  }

  @Override
  protected AuthorizationManager getAuthorizationManager() {
    return authorizationManager;
  }

  @Override
  protected ProviderService<LibraryStrategyType> getService() {
    return libraryStrategyService;
  }

  @Override
  protected LibraryStrategyTypeDto toDto(LibraryStrategyType object) {
    return Dtos.asDto(object);
  }

  @Override
  protected LibraryStrategyTypeDto makeDto() {
    return new LibraryStrategyTypeDto();
  }

}
