/*
 * Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
 * MISO project contacts: Robert Davey @ TGAC
 * *********************************************************************
 *
 * This file is part of MISO.
 *
 * MISO is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MISO is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MISO.  If not, see <http://www.gnu.org/licenses/>.
 *
 * *********************************************************************
 */

WarningTarget.sample = {
    headerWarnings: function(sample){
      var warnings = [];
      warnings = Warning.addWarnings([
        [sample.subprojectPriority, 'Belongs to high priority subproject \'' + sample.subprojectAlias + '\''],
        [sample.identityConsentLevel === 'Revoked', 'Donor has revoked consent'],
        ], warnings);
      return Warning.generateHeaderWarnings(warnings);
    },
    tableWarnings: function(data, type, sample){
      var warnings = [];
      warnings = Warning.addWarnings([
        [sample.subprojectPriority, 'PRIORITY (' + sample.subprojectAlias + ')'],
        [sample.identityConsentLevel === 'Revoked', '(CONSENT REVOKED)'],
        ], warnings);
      return Warning.generateTableWarnings(data, warnings);
    },
};