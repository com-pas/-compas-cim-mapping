// SPDX-FileCopyrightText: 2021 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.cim.mapping.mapper;

import com.powsybl.cgmes.model.CgmesModel;
import com.powsybl.triplestore.api.PropertyBag;
import com.powsybl.triplestore.api.PropertyBags;
import com.powsybl.triplestore.api.TripleStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl2007b4.model.TConnectivityNode;
import org.lfenergy.compas.scl2007b4.model.TSubstation;
import org.lfenergy.compas.scl2007b4.model.TVoltageLevel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.cim.mapping.mapper.CimToSclMapperContext.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CimToSclMapperContextTest {
    @Mock
    private CgmesModel cgmesModel;

    @InjectMocks
    private CimToSclMapperContext context;

    @Test
    void getSubstations_WhenCalled_ThenPropertyBagsIsConvertedToCgmesSubstation() {
        var substationId = "SubstationId";
        var substationName = "Name Substation";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(SUBSTATION_PROP, NAME_PROP));
        bag.put(SUBSTATION_PROP, substationId);
        bag.put(NAME_PROP, substationName);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getSubstations();
        assertNotNull(result);
        assertEquals(1, result.size());
        var substation = result.get(0);
        assertEquals(substationId, substation.getId());
        assertEquals(substationName, substation.getName());
    }

    @Test
    void getVoltageLevelsBySubstation_WhenCalledWithKnownId_ThenPropertyBagsIsFilteredOnIdAndConvertedToCgmesVoltageLevel() {
        var voltageLevelId = "VoltageLevelId";
        var voltageLevelName = "Name VoltageLevel";
        var substationId = "Known Substation ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(VOLTAGE_LEVEL_PROP, NAME_PROP, NOMINAL_VOLTAGE_PROP, SUBSTATION_PROP));
        bag.put(VOLTAGE_LEVEL_PROP, voltageLevelId);
        bag.put(NAME_PROP, voltageLevelName);
        bag.put(NOMINAL_VOLTAGE_PROP, "1.0");
        bag.put(SUBSTATION_PROP, substationId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getVoltageLevelsBySubstation(substationId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var voltageLevel = result.get(0);
        assertEquals(voltageLevelId, voltageLevel.getId());
        assertEquals(voltageLevelName, voltageLevel.getName());
    }

    @Test
    void getBusbarSectionsByEquipmentContainer_WhenSparQLReturnsBags_ThenPropertyBagIsConvertedToCgmesBusbarSection() {
        var busbarSectionId = "BusbarSectionId";
        var busbarSectionName = "Name BusbarSection";
        var equipmentContainerId = "ContainerId";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(BUSBARSECTION_PROP, NAME_PROP, EQUIPMENT_CONTAINER_PROP));
        bag.put(BUSBARSECTION_PROP, busbarSectionId);
        bag.put(NAME_PROP, busbarSectionName);
        bag.put(EQUIPMENT_CONTAINER_PROP, equipmentContainerId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getBusbarSectionsByEquipmentContainer(equipmentContainerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var busbarSection = result.get(0);
        assertEquals(busbarSectionId, busbarSection.getId());
        assertEquals(busbarSectionName, busbarSection.getName());
    }

    @Test
    void getBaysByVoltageLevel_WhenSparQLReturnsBags_ThenPropertyBagIsConvertedToCgmesBay() {
        var bayId = "BayId";
        var bayName = "Name Bay";
        var voltageLevelId = "VoltageLevelId";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(BAY_PROP, NAME_PROP, VOLTAGE_LEVEL_PROP));
        bag.put(BAY_PROP, bayId);
        bag.put(NAME_PROP, bayName);
        bag.put(VOLTAGE_LEVEL_PROP, voltageLevelId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getBaysByVoltageLevel(voltageLevelId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var bay = result.get(0);
        assertEquals(bayId, bay.getId());
        assertEquals(bayName, bay.getName());
    }

    @Test
    void getTransformers_WhenSparQLReturnsBags_ThenPropertyBagIsConvertedToCgmesTransformer() {
        var pwId = "PowertransformerId";
        var pwName = "Name Powertransformer";
        var pwDesc = "Desc Powertransformer";
        var pwContainerId = "Known Container ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(POWER_TRANSFORMER_PROP, NAME_PROP, DESCRIPTION_PROP, EQUIPMENT_CONTAINER_PROP));
        bag.put(POWER_TRANSFORMER_PROP, pwId);
        bag.put(NAME_PROP, pwName);
        bag.put(DESCRIPTION_PROP, pwDesc);
        bag.put(EQUIPMENT_CONTAINER_PROP, pwContainerId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getTransformers(pwContainerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var transformer = result.get(0);
        assertEquals(pwId, transformer.getId());
        assertEquals(pwName, transformer.getName());
        assertEquals(pwDesc, transformer.getDescription());
    }

    @Test
    void getTransformerEnds_WhenCalledWithKnownId_ThenPropertyBagsIsFilteredOnIdAndConvertedToCgmesTransformerEnd() {
        var tfeId = "TfeId";
        var tfeName = "Name Tfe";
        var terminalId = "Known Terminal ID";
        var tfId = "Known Transformer ID";
        var endNumber = "1";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(TRANSFORMER_END_PROP, NAME_PROP, POWER_TRANSFORMER_PROP, TERMINAL_PROP, END_NUMBER_PROP));
        bag.put(TRANSFORMER_END_PROP, tfeId);
        bag.put(NAME_PROP, tfeName);
        bag.put(POWER_TRANSFORMER_PROP, tfId);
        bag.put(TERMINAL_PROP, terminalId);
        bag.put(END_NUMBER_PROP, endNumber);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getTransformerEnds(tfId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var ccn = result.get(0);
        assertEquals(tfeId, ccn.getId());
        assertEquals(tfeName + "_" + endNumber, ccn.getUniqueName());
        assertEquals(tfeName, ccn.getName());
        assertEquals(terminalId, ccn.getTerminalId());
        assertEquals(endNumber, ccn.getEndNumber());
    }

    @Test
    void getTapChanger_WhenNoTapChangersFound_ThenEmptyOptionalReturned() {
        var tfeId = "Known Transformer End ID";

        var ratioBags = new PropertyBags();
        var phaseBags = new PropertyBags();

        setupTripleStore(ratioBags, phaseBags);

        var result = context.getTapChanger(tfeId);
        assertFalse(result.isPresent());
    }

    @Test
    void getTapChanger_WhenRatioTapChangersFound_ThenConvertedRatioTapChangerReturned() {
        var tcId = "TapChangerId";
        var tcName = "Name TapChanger";
        var tfeId = "Known Transformer End ID";

        var ratioBags = new PropertyBags();

        var bag = new PropertyBag(List.of(RATIO_TAP_CHANGER_PROP, NAME_PROP, TRANSFORMER_END_PROP));
        bag.put(RATIO_TAP_CHANGER_PROP, tcId);
        bag.put(NAME_PROP, tcName);
        bag.put(TRANSFORMER_END_PROP, tfeId);
        ratioBags.add(bag);

        setupTripleStore(ratioBags);

        var result = context.getTapChanger(tfeId);
        assertTrue(result.isPresent());
        var tapChanger = result.get();
        assertEquals(tcId, tapChanger.getId());
        assertEquals(tcName, tapChanger.getName());
    }

    @Test
    void getTapChanger_WhenNoRatioTapChangerFoundButPhaseTapChangersFound_ThenConvertedPhaseTapChangerReturned() {
        var tcId = "TapChangerId";
        var tcName = "Name TapChanger";
        var tfeId = "Known Transformer End ID";

        var ratioBags = new PropertyBags();
        var phaseBags = new PropertyBags();

        var bag = new PropertyBag(List.of(PHASE_TAP_CHANGER_PROP, NAME_PROP, TRANSFORMER_END_PROP));
        bag.put(PHASE_TAP_CHANGER_PROP, tcId);
        bag.put(NAME_PROP, tcName);
        bag.put(TRANSFORMER_END_PROP, tfeId);
        phaseBags.add(bag);

        setupTripleStore(ratioBags, phaseBags);

        var result = context.getTapChanger(tfeId);
        assertTrue(result.isPresent());
        var tapChanger = result.get();
        assertEquals(tcId, tapChanger.getId());
        assertEquals(tcName, tapChanger.getName());
    }

    @Test
    void getConnectivityNodeByBusbarSection_WhenCalledWithKnownId_ThenPropertyBagsIsConvertedToCgmesConnectivityNode() {
        var ccnId = "CcnId";
        var ccnName = "Name Ccn";
        var busbarSectionId = "BusbarSection ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(CONNECTIVITY_NODE_PROP, NAME_PROP, CONDUCTING_EQUIPMENT_PROP));
        bag.put(CONNECTIVITY_NODE_PROP, ccnId);
        bag.put(NAME_PROP, ccnName);
        bag.put(CONDUCTING_EQUIPMENT_PROP, busbarSectionId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getConnectivityNodeByBusbarSection(busbarSectionId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var ccn = result.get(0);
        assertEquals(ccnId, ccn.getId());
        assertEquals(ccnName, ccn.getName());
    }

    @Test
    void getConnectivityNodeByBay_WhenCalledWithKnownId_ThenPropertyBagsIsConvertedToCgmesConnectivityNode() {
        var ccnId = "CcnId";
        var ccnName = "Name Ccn";
        var bayID = "Bay ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(CONNECTIVITY_NODE_PROP, NAME_PROP, EQUIPMENT_CONTAINER_PROP));
        bag.put(CONNECTIVITY_NODE_PROP, ccnId);
        bag.put(NAME_PROP, ccnName);
        bag.put(EQUIPMENT_CONTAINER_PROP, bayID);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getConnectivityNodeByBay(bayID);
        assertNotNull(result);
        assertEquals(1, result.size());
        var ccn = result.get(0);
        assertEquals(ccnId, ccn.getId());
        assertEquals(ccnName, ccn.getName());
    }

    @Test
    void getSwitches_WhenCalledWithKnownId_ThenPropertyBagsIsFilteredOnIdAndConvertedToCgmesSwitch() {
        var switchId = "SwitchId";
        var switchName = "Name Switch";
        var containerId = "Known Container ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(SWITCH_PROP, NAME_PROP, TYPE_PROP, EQUIPMENT_CONTAINER_PROP));
        bag.put(SWITCH_PROP, switchId);
        bag.put(NAME_PROP, switchName);
        bag.put(TYPE_PROP, "Breaker");
        bag.put(EQUIPMENT_CONTAINER_PROP, containerId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getSwitches(containerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var switchEquipment = result.get(0);
        assertEquals(switchId, switchEquipment.getId());
        assertEquals(switchName, switchEquipment.getName());
    }

    @Test
    void getTerminalsByConductingEquipment_WhenCalledWithKnownId_ThenPropertyBagsIsFilteredOnIdAndConvertedToCgmesTerminal() {
        var terminalId = "TerminalId";
        var terminalName = "Name Terminal";
        var ccnNode = "Connectivity Node ID";
        var containerId = "Known Container ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(TERMINAL_PROP, NAME_PROP, CONNECTIVITY_NODE_PROP));
        bag.put(TERMINAL_PROP, terminalId);
        bag.put(NAME_PROP, terminalName);
        bag.put(CONNECTIVITY_NODE_PROP, ccnNode);
        bag.put(CONDUCTING_EQUIPMENT_PROP, containerId);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getTerminalsByConductingEquipment(containerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        var terminal = result.get(0);
        assertEquals(terminalId, terminal.getId());
        assertEquals(terminalName, terminal.getName());
        assertEquals(ccnNode, terminal.getConnectivityNodeId());
    }


    @Test
    void getTerminalById_WhenCalledWithKnownId_ThenPropertyBagsIsFilteredOnIdAndConvertedToCgmesTerminal() {
        var terminalId = "TerminalId";
        var terminalName = "Name Terminal";
        var ccnNode = "Connectivity Node ID";

        var bags = new PropertyBags();
        var bag = new PropertyBag(List.of(TERMINAL_PROP, NAME_PROP, CONNECTIVITY_NODE_PROP));
        bag.put(TERMINAL_PROP, terminalId);
        bag.put(NAME_PROP, terminalName);
        bag.put(CONNECTIVITY_NODE_PROP, ccnNode);
        bags.add(bag);

        setupTripleStore(bags);

        var result = context.getTerminalById(terminalId);
        assertNotNull(result);
        var terminal = result.get();
        assertEquals(terminalId, terminal.getId());
        assertEquals(terminalName, terminal.getName());
        assertEquals(ccnNode, terminal.getConnectivityNodeId());
    }

    @Test
    void createPathName_WhenCalledWithNoStack_ThenEmptyStringIsReturned() {
        assertEquals("", context.createPathName());
    }

    @Test
    void createPathName_WhenCalledWithStack_ThenPathNameIsReturned() {
        var firstPartname = "Name 1";
        var tSubstation = new TSubstation();
        tSubstation.setName(firstPartname);
        context.addLast(tSubstation);

        var secondPartname = "Name 2";
        var tVoltageLevel = new TVoltageLevel();
        tVoltageLevel.setName(secondPartname);
        context.addLast(tVoltageLevel);

        // First look what is returned when both elements are on the stack.
        assertEquals(firstPartname + "/" + secondPartname, context.createPathName());

        // Now pop one from the stack and see what the name is then.
        context.removeLast();
        assertEquals(firstPartname, context.createPathName());
    }

    @Test
    void resetTConnectivityNodeMap_WhenCalledAfterRest_ThenPathNameOfCNCannotBeFoundAnymore() {
        var cnId = "CN ID";
        var cnPathName = "CN PATH NAME";
        var cn = new TConnectivityNode();
        cn.setPathName(cnPathName);

        context.saveTConnectivityNode(cnId, cn);
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());

        context.resetTConnectivityNodeMap();
        var result = context.getPathnameFromConnectivityNode(cnId);

        assertFalse(result.isPresent());
    }

    @Test
    void containsTConnectivityNode_WhenCalledWithKnownId_ThenTrueReturned() {
        var knownCnId = "CN ID";

        context.saveTConnectivityNode(knownCnId, new TConnectivityNode());
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        assertTrue(context.containsTConnectivityNode(knownCnId));
    }

    @Test
    void containsTConnectivityNode_WhenCalledWithUnknownId_ThenFalseReturned() {
        context.saveTConnectivityNode("CN ID", new TConnectivityNode());
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        assertFalse(context.containsTConnectivityNode("Unknown ID"));
    }

    @Test
    void getPathnameFromConnectivityNode_WhenCalledWithKnownId_ThenPathNameOfCNReturned() {
        var cnId = "CN ID";
        var cnPathName = "CN PATH NAME";
        var cn = new TConnectivityNode();
        cn.setPathName(cnPathName);

        context.saveTConnectivityNode(cnId, cn);
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        var result = context.getPathnameFromConnectivityNode(cnId);

        assertTrue(result.isPresent());
        assertEquals(cnPathName, result.get());
    }

    @Test
    void getPathnameFromConnectivityNode_WhenCalledWithUnKnownId_ThenEmptyOptionalReturned() {
        var cnId = "CN ID";

        context.saveTConnectivityNode("Unknown ID", new TConnectivityNode());
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        var result = context.getPathnameFromConnectivityNode(cnId);

        assertFalse(result.isPresent());
    }

    @Test
    void getNameFromConnectivityNode_WhenCalledWithKnownId_ThenPathNameOfCNReturned() {
        var cnId = "CN ID";
        var cnName = "CN NAME";
        var cn = new TConnectivityNode();
        cn.setName(cnName);

        context.saveTConnectivityNode(cnId, cn);
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        var result = context.getNameFromConnectivityNode(cnId);

        assertTrue(result.isPresent());
        assertEquals(cnName, result.get());
    }

    @Test
    void getNameFromConnectivityNode_WhenCalledWithUnKnownId_ThenEmptyOptionalReturned() {
        var cnId = "CN ID";

        context.saveTConnectivityNode("Unknown ID", new TConnectivityNode());
        context.saveTConnectivityNode("Other ID", new TConnectivityNode());
        var result = context.getNameFromConnectivityNode(cnId);

        assertFalse(result.isPresent());
    }

    private void setupTripleStore(PropertyBags bags, PropertyBags... otherBags) {
        var tripleStore = mock(TripleStore.class);
        when(cgmesModel.tripleStore()).thenReturn(tripleStore);
        when(tripleStore.query(anyString())).thenReturn(bags, otherBags);
    }
}