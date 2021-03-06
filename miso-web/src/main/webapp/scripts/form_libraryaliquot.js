if (typeof FormTarget === 'undefined') {
  FormTarget = {};
}
FormTarget.libraryaliquot = (function($) {

  /*
   * Expected config {
   *   detailedSample: boolean
   * }
   */

  return {
    getSaveUrl: function(aliquot) {
      if (aliquot.id) {
        return '/miso/rest/libraryaliquots/' + aliquot.id;
      } else {
        throw new Error('Page not intended for new library aliquot creation');
      }
    },
    getSaveMethod: function(aliquot) {
      return 'PUT';
    },
    getEditUrl: function(aliquot) {
      return '/miso/libraryaliquot/' + aliquot.id;
    },
    getSections: function(config, object) {
      return [{
        title: 'Library Aliquot Information',
        fields: [{
          title: 'Library Aliquot ID',
          data: 'id',
          type: 'read-only'
        }, {
          title: 'Name',
          data: 'name',
          type: 'read-only'
        }, {
          title: 'Parent Library Aliquot',
          data: 'parentAliquotId',
          include: !!object.parentAliquotId,
          type: 'read-only',
          getDisplayValue: function(aliquot) {
            return aliquot.parentAliquotAlias;
          },
          getLink: function(aliquot) {
            return Urls.ui.libraryAliquots.edit(aliquot.parentAliquotId);
          }
        }, {
          title: 'Parent Library',
          data: 'libraryId',
          include: !object.parentAliquotId,
          type: 'read-only',
          getDisplayValue: function(aliquot) {
            return aliquot.libraryAlias;
          },
          getLink: function(aliquot) {
            return Urls.ui.libraries.edit(aliquot.libraryId);
          }
        }, {
          title: 'Alias',
          data: 'alias',
          type: 'text',
          required: true,
          maxLength: 100
        }, {
          title: 'Matrix Barcode',
          data: 'identificationBarcode',
          type: 'text',
          maxLength: 255
        }, {
          title: 'Design Code',
          data: 'libraryDesignCodeId',
          type: 'dropdown',
          include: config.detailedSample,
          required: true,
          getSource: function() {
            return Constants.libraryDesignCodes;
          },
          sortSource: Utils.sorting.standardSort('code'),
          getItemLabel: function(item) {
            return item.code + ' (' + item.description + ')';
          },
          getItemValue: function(item) {
            return item.id;
          },
          onChange: function(newValue, form) {
            var designCode = Utils.array.findUniqueOrThrow(Utils.array.idPredicate(newValue), Constants.libraryDesignCodes);
            form.updateField('targetedSequencingId', {
              required: designCode.targetedSequencingRequired
            });
          }
        }, {
          title: 'Size (bp)',
          data: 'dnaSize',
          type: 'int',
          maxLength: 10,
          min: 1
        }, {
          title: 'Discarded',
          data: 'discarded',
          type: 'checkbox',
          onChange: function(newValue, form) {
            form.updateField('volume', {
              disabled: newValue
            });
          }
        }, {
          title: 'Volume',
          data: 'volume',
          type: 'decimal'
        }, FormUtils.makeUnitsField(object, 'volume'), {
          title: 'Concentration',
          data: 'concentration',
          type: 'decimal'
        }, FormUtils.makeUnitsField(object, 'concentration'), FormUtils.makeBoxLocationField(), {
          title: 'Creation Date',
          data: 'creationDate',
          required: 'true',
          type: 'date'
        }, {
          title: 'Targeted Sequencing',
          data: 'targetedSequencingId',
          type: 'dropdown',
          getSource: function() {
            return Constants.targetedSequencings.filter(function(targetedSequencing) {
              return targetedSequencing.kitDescriptorIds.indexOf(object.libraryKitDescriptorId) > -1;
            });
          },
          sortSource: Utils.sorting.standardSort('alias'),
          getItemLabel: function(item) {
            return item.alias;
          },
          getItemValue: function(item) {
            return item.id;
          }
        }, {
          title: 'Parent ng Used',
          data: 'ngUsed',
          type: 'decimal'
        }, {
          title: 'Parent Volume Used',
          data: 'volumeUsed',
          type: 'decimal'
        }].concat(FormUtils.makeDistributionFields())
      }, {
        title: 'Details',
        include: config.detailedSample,
        fields: [{
          title: 'Effective Group ID',
          data: 'effectiveGroupId',
          type: 'read-only',
          getDisplayValue: function(library) {
            if (library.hasOwnProperty('effectiveGroupId') && library.effectiveGroupId !== null) {
              return library.effectiveGroupId + ' (' + library.effectiveGroupIdSample + ')';
            } else {
              return 'None';
            }
          }
        }, {
          title: 'Group ID',
          data: 'groupId',
          type: 'text',
          maxLength: 100,
          regex: Utils.validation.alphanumRegex
        }, {
          title: 'Group Description',
          data: 'groupDescription',
          type: 'text',
          maxLength: 255
        }]
      }];
    }
  };

})(jQuery);
