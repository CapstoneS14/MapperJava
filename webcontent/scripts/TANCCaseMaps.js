    var mapFreq = new HashMap(); //store frequency of a type record on the map
    var mapValue; //store accumulated value of each type of record on the map
    
    if (usOnlyMapFilter === false) {
        InitiateWorldMap(mapFreq);
        if (valueFilter) {
            mapValue = new HashMap();
            InitiateWorldMap(mapValue);
        }
    } else {	//calculate frequency of each state on the us map
        InitiateUSAMap(mapFreq);
        if (valueFilter) {
            mapValue = new HashMap();
            InitiateUSAMap(mapValue);
        }
    }
    var EUlist = ["Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark",
        "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia",
        "Lithuania", "Luxembourg", "Malta", "Netherlands","Poland", "Portugal", "Romania", "Slovak Republic",
        "Slovenia", "Spain", "Sweden", "United Kingdom"];
    
    // go through each record
    for (var record in data) {
        
        // case date initiate filter section
        if (dateInitiateFilterFrom !== null || dateInitiateFilterTo !== null) {
            var tmp1 = data[record]["Case Initiated Date"].split(" "); // remove time and keep only date
            var tmp2 = tmp1[0].split("/"); // split mon/day/year
            var caseIniDay = parseInt(tmp2[1]);
            var caseIniMon = parseInt(tmp2[0]);
            var caseIniYear = parseInt(tmp2[2]);
            
            //skip record which case date is earlier than the filter from-date
            if (dateInitiateFilterFrom !== null) {
                var tmp = dateInitiateFilterFrom.split("/");
                var filterIniDayFrom = parseInt(tmp[1]);
                var filterIniMonFrom = parseInt(tmp[0]);
                var filterIniYearFrom = parseInt(tmp[2]);
                
                if (caseIniYear < filterIniYearFrom) {
                    continue;
                }
                
                if (caseIniYear === filterIniYearFrom && caseIniMon < filterIniMonFrom) {
                    continue;
                }
                
                if (caseIniYear === filterIniYearFrom && caseIniMon === filterIniMonFrom && caseIniDay < filterIniDayFrom) {
                    continue;
                }
            }
		
            //skip record which case date is later than the filter to-date
            if (dateInitiateFilterTo !== null) {
                var tmp = dateInitiateFilterTo.split("/");
                var filterIniDayTo = parseInt(tmp[1]);
                var filterIniMonTo = parseInt(tmp[0]);
                var filterIniYearTo = parseInt(tmp[2]);
                
                if (caseIniYear > filterIniYearTo) {
                    continue;
                }
                
                if (caseIniYear === filterIniYearTo && caseIniMon > filterIniMonTo) {
                    continue;
                }
                
                if (caseIniYear === filterIniYearTo && caseIniMon === filterIniMonTo && caseIniDay > filterIniDayTo) {
                    continue;
                }
            }
        }
	  
        // case date close filter section
        if (dateCloseFilterFrom !== null || dateCloseFilterTo !== null) {
            var tmp1 = data[record]["Date Closed"].split(" "); // remove time and keep only date
            var tmp2 = tmp1[0].split("/"); // split mon/day/year
            var caseCloseDay = parseInt(tmp2[1]);
            var caseCloseMon = parseInt(tmp2[0]);
            var caseCloseYear = parseInt(tmp2[2]);
            
            //skip record which case date is earlier than the filter from-date
            if (dateCloseFilterFrom !== null) {
                var tmp = dateCloseFilterFrom.split("/");
                var filterCloseDayFrom = parseInt(tmp[1]);
                var filterCloseMonFrom = parseInt(tmp[0]);
                var filterCloseYearFrom = parseInt(tmp[2]);
                
                if (caseCloseYear < filterCloseYearFrom) {
                    continue;
                }
                
                if (caseCloseYear === filterCloseYearFrom && caseCloseMon < filterCloseMonFrom) {
                    continue;
                }
                
                if (caseCloseYear === filterCloseYearFrom && caseCloseMon === filterCloseMonFrom && caseCloseDay < filterCloseDayFrom) {
                    continue;
                }
            }
            
            //skip record which case date is later than the filter to-date
            if (dateCloseFilterTo !== null) {
                var tmp = dateCloseFilterTo.split("/");
                var filterCloseDayTo = parseInt(tmp[1]);
                var filterCloseMonTo = parseInt(tmp[0]);
                var filterCloseYearTo = parseInt(tmp[2]);
                
                if (caseCloseYear > filterCloseYearTo) {
                    continue;
                }
                
                if (caseCloseYear === filterCloseYearTo && caseCloseMon > filterCloseMonTo) {
                    continue;
                }
                
                if (caseCloseYear === filterCloseYearTo && caseCloseMon === filterCloseMonTo && caseCloseDay > filterCloseDayTo) {
                    continue;
                }
            }
        }
        
        // skip records if success value is false success filter section, assign the success date to case close date filter
        if (successFilter === true && (data[record]["Status"] !== "Closed" || data[record]["Success"] === "FALSE")) {
            continue;
        }
        
        // sme involved filter section, default should be false.
        if (smeFilter === true && data[record]["SME Involved"] === "No") {
            continue;
        }
        
        // industry filter section, default is null, assuming the value is a string
        if (industryFilter !== null) {
            var curIndustry = data[record]["Industry/Sectors"];
            if (curIndustry !== industryFilter) {
                continue;
            }
        }
        
        // issueFilter section
        if (issueFilter !== null) {
            var curIssue = data[record]["Issue(s)"];
            if (curIssue.indexOf(issueFilter) < 0 ) {
                continue;
            }
        }
        
        // publicity permission filter section, boolean, default = false
        if (publicityPermissionFilter === true && data[record]["Publicity Permission"] !== "Granted") {
            continue;
        }
        
        // FTA relevant filter
        if (ftaFilter !== null) {
            var ftaStr = data[record]["Trade Agreement(s)"];
            if (ftaStr.indexOf(ftaFilter) < 0) {
                continue;
            }
        }
        
        //WTO relevant filter
        if (wtoFilter !== null) {
            var wtoStr = data[record]["Trade Agreement(s)"];
            if (wtoStr.indexOf(wtoFilter) < 0) {
                continue;
            }
        }
        
        var NumOfInvalidRecord = 0;
        
        //default (false) world map
        if (usOnlyMapFilter === false) {
            var country = data[record]["Country(s)"];
            if (country.indexOf(",") < 0 && country !== "European Union") {
                var cty = countryAbbreviationTable.get(country);
                if (cty === null) {
                    document.write("Invalid record: " + country + "<br>");
                    NumOfInvalidRecord = NumOfInvalidRecord + 1;
                }
                var tmp = mapFreq.get(cty) + 1;
                mapFreq.put(cty, tmp);
                
                if (valueFilter) {
                    var curVal = data[record]["Case Value (in $ millions)"];
                    var tmpVal = parseInt(curVal) + mapValue.get(cty);
                    mapValue.put(cty, tmpVal);
                }
            } else if (country.indexOf("European Union") >= 0) { // EU
                var countries = EUlist;
                for (var i in countries) {
                    var cty = countryAbbreviationTable.get(countries[i]);
                    if (cty === null) {
                        document.write("Invalid record: " + countries[i] + "<br>");
                        NumOfInvalidRecord = NumOfInvalidRecord + 1;
                    }
                    var tmp = mapFreq.get(cty) + 1;
                    mapFreq.put(cty, tmp);
                    if (valueFilter) {
                        var curVal = data[record]["Case Value (in $ millions)"];
                        var tmpVal = parseInt(curVal) + mapValue.get(cty);
                        mapValue.put(cty, tmpVal);
                    }
                }
            } else if (country.indexOf(",") >= 0) {
                var countries = country.split(", ");
                for (var i in countries) {
                    var cty = countryAbbreviationTable.get(countries[i]);
                    if (cty == null) {
                        document.write("Invalid record: " + countries[i] + "<br>");
                        NumOfInvalidRecord = NumOfInvalidRecord + 1;
                    }
                    var tmp = mapFreq.get(cty) + 1;
                    mapFreq.put(cty, tmp);
                    if (valueFilter) {
                        var curVal = data[record]["Case Value (in $ millions)"];
                        var tmpVal = parseInt(curVal) + mapValue.get(cty);
                        mapValue.put(cty, tmpVal);
                    }
                    
                }
            }
        } else { // us map
            var state = data[record]["Case Address: State"];
            if (!mapFreq.containsKey(state)) {
                NumOfInvalidRecord = NumOfInvalidRecord + 1;
            } else {
                var tmp = mapFreq.get(state) + 1;
                mapFreq.put(state, tmp);
                if (valueFilter) {
                    var curVal = data[record]["Case Value (in $ millions)"];
                    var tmpVal = parseInt(curVal) + mapValue.get(cty);
                    mapValue.put(cty, tmpVal);
                }
            }
        }
    }
    