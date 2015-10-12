package com.movilizer.connector.bpost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.movilitas.movilizer.v14.MovilizerMovelet;
import com.movilitas.movilizer.v14.MovilizerParticipant;
import com.movilitas.movilizer.v14.MovilizerParticipantConfiguration;
import com.movilizer.connector.model.PasswordTypes;
import com.movilizer.connector.service.client.MovilizerClient;
import com.movilizer.connector.service.mapper.DataContainerEvaluator;

@Component
public class BPostConnector {

	private final Log logger = LogFactory.getLog(getClass());



	@Autowired
	MovilizerClient movilizer;

	@Autowired
	DataContainerEvaluator evaluator;

	@Autowired
	CSVUtility utilityCSV;

	@Value("${assignmentconfig.dir}")
	private String configFilesDir;

	int assignments = 0;

	int unassignments = 0;

	/**
	 * Assign everything to bpost
	 */
	public void runFullConfig() {
		Map<String, MoveletListAssignment> moveletListAssignments = generateMoveletListAssignments();
		List<MoveletGroupAssignment> groupAssignments = generategroupAssignments("assignment-group");
		List<MoveletGroupAssignment> groupUnassignments = generategroupAssignments("unassignment-group");

		int index = 0;
		for (Map.Entry<MovilizerParticipant, MovilizerParticipantConfiguration> participantEntry : createParticipantsFromCSV("import")
				.entrySet()) {
			MovilizerParticipant participant = participantEntry.getKey();
			//movilizer.assignConfigurationToParticipant(participantEntry.getValue());

			assignMoveletLists(participant, moveletListAssignments);
			assignToGroups(participant, groupAssignments);
			unassignToGroups(participant, groupUnassignments);
			
			index++;
			if(index%1000 == 0)
			{
				movilizer.perfomSyncToCloud();
			}
		}

		logger.info(assignments + " assignments.");
		logger.info(unassignments + " unassignments.");

		movilizer.perfomSyncToCloud();
	}

	private void assignToGroups(MovilizerParticipant participant, List<MoveletGroupAssignment> groupAssignments) {
		for(MoveletGroupAssignment moveletAssignment : groupAssignments)
		{
			for(String groupPrefix : moveletAssignment.getGroups())
			{
				if(participant.getName() != null && participant.getName().startsWith(groupPrefix))
				{
					assignments++;
					movilizer.assignMoveletToParticipant(moveletAssignment.getMoveletKey(), participant);
				}
			}
		}
	}

	private void unassignToGroups(MovilizerParticipant participant, List<MoveletGroupAssignment> groupAssignments) {
		for(MoveletGroupAssignment moveletAssignment : groupAssignments)
		{
			for(String groupPrefix : moveletAssignment.getGroups())
			{
				if(participant.getName() != null && participant.getName().startsWith(groupPrefix))
				{
					unassignments++;
					movilizer.unassignMoveletToParticipant(moveletAssignment.getMoveletKey(), participant);
				}
			}
		}
	}

	private void assignMoveletLists(MovilizerParticipant participant,
			Map<String, MoveletListAssignment> moveletListAssignments) {
		MoveletListAssignment tMoveletListAssignment = moveletListAssignments.get(participant.getDeviceAddress());
		if (tMoveletListAssignment != null) {
			for (String moveletKey : tMoveletListAssignment.getMoveletKeys()) {
				assignments++;
				movilizer.assignMoveletToParticipant(moveletKey, participant);
			}
		}
	}

	private Map<String, MoveletListAssignment> generateMoveletListAssignments() {
		Map<String, MoveletListAssignment> result = new HashMap<String, MoveletListAssignment>();
		for (Entry<String, String[]> entry : utilityCSV
				.createMapFromCSV(configFilesDir + "assignment-participant.csv").entrySet()) {
			MoveletListAssignment tMoveletListAssignment = new MoveletListAssignment();
			String deviceAddress = "@" + entry.getKey().trim() + "@bpostmeap.be";
			tMoveletListAssignment.setDeviceAddress(deviceAddress);
			int i = 0;
			for (String moveletKey : entry.getValue()) {
				if (i != 0) {
					tMoveletListAssignment.getMoveletKeys().add(moveletKey);
				}
				i++;
			}
			result.put(deviceAddress, tMoveletListAssignment);
		}
		return result;
	}


	public List<MoveletGroupAssignment> generategroupAssignments(String csvfile)
	{
		List<MoveletGroupAssignment> result = new ArrayList<MoveletGroupAssignment>();
		for (Entry<String, String[]> entry : utilityCSV
				.createMapFromCSV(configFilesDir+csvfile+".csv").entrySet()) {
			MoveletGroupAssignment currentAssignment = new MoveletGroupAssignment();
			currentAssignment.setMoveletKey(entry.getKey());
			currentAssignment.setGroups(Arrays.asList(entry.getValue()));

			result.add(currentAssignment);
		}
		return result;
	}

	public void createAndAssignFromCSV() {
		MovilizerMovelet movelet1 = movilizer
				.unmarshallMoveletFromFile("/bpost/movelets/contract-accelerator-v10-fr-int.mxml");
		MovilizerMovelet movelet2 = movilizer
				.unmarshallMoveletFromFile("/bpost/movelets/contract-accelerator-v10-fr.mxml");
		movilizer.createMovelet(movelet1);
		movilizer.createMovelet(movelet2);
		movilizer.removeMovelet("75cc5a06-2f06-4f9b-aac9-dc54f9a5c34b", "", true);
		for (Entry<String, String[]> entry : utilityCSV.createMapFromCSV(configFilesDir + "import.csv")
				.entrySet()) {

			MovilizerParticipant participant = new MovilizerParticipant();
			participant.setDeviceAddress("@" + entry.getKey() + "@bpostmeap.be");
			participant.setName("test");
			participant.setParticipantKey(entry.getKey() + "@bpostmeap.be");
			movilizer.assignMoveletToParticipant(movelet1.getMoveletKey(), participant);
			movilizer.assignMoveletToParticipant(movelet2.getMoveletKey(), participant);
		}
	}

	private void createOneOffParticipants() {
		MovilizerMovelet movelet = movilizer.unmarshallMoveletFromFile("/bpost/test.mxml");
		movilizer.createMovelet(movelet);

		for (Map.Entry<MovilizerParticipant, MovilizerParticipantConfiguration> participantEntry : createParticipantsFromCSV("import")
				.entrySet()) {
			MovilizerParticipant participant = participantEntry.getKey();
			movilizer.assignMoveletToParticipant(movelet.getMoveletKey(), participant);
			movilizer.assignConfigurationToParticipant(participantEntry.getValue());
		}

		movilizer.perfomSyncToCloud();
	}

	private Map<MovilizerParticipant, MovilizerParticipantConfiguration> createParticipantsFromCSV(String csvFileName) {
		Map<MovilizerParticipant, MovilizerParticipantConfiguration> result = new HashMap<MovilizerParticipant, MovilizerParticipantConfiguration>();
		for (Entry<String, String[]> entry : utilityCSV.createMapFromCSV(configFilesDir + csvFileName + ".csv")
				.entrySet()) {
			String deviceAddress = entry.getKey();
			String name = entry.getValue()[1];
			String participantKey = entry.getKey().replaceFirst("@", "").replace("+", "");
			MovilizerParticipant tMovilizerParticipant = new MovilizerParticipant();
			tMovilizerParticipant.setDeviceAddress(deviceAddress);
			tMovilizerParticipant.setName(name);
			tMovilizerParticipant.setParticipantKey(participantKey);

			MovilizerParticipantConfiguration movilizerParticipantConf = new MovilizerParticipantConfiguration();

			movilizerParticipantConf.setDeviceAddress(deviceAddress);
			if(entry.getValue().length > 2)
			{
				movilizerParticipantConf.setPasswordHashType(PasswordTypes.PLAIN_TEXT_PASSWORD.getValue());
				movilizerParticipantConf.setPasswordHashValue(entry.getValue()[2]);
			}
			movilizerParticipantConf.setName(name);

			result.put(tMovilizerParticipant, movilizerParticipantConf);
		}
		return result;
	}

}
