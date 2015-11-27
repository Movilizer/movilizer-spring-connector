package com.movilizer.connector.bpost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.movilitas.movilizer.v14.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.movilizer.connector.model.PasswordTypes;
import com.movilizer.connector.service.OXMUtility;
import com.movilizer.connector.service.client.MovilizerClient;

@Service
public class AssignmentBoardUtility {

    private final Log logger = LogFactory.getLog(getClass());

    private static final String HIERARCHY_ATTRIBUTE = "MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY";
    private static final String HASH_ATTRIBUTE_GROUP = "MOVILIZER_ASSIGNMENT_BOARD_ADDITIONAL";
    private static final String HASH_ATTRIBUTE = "HASH";

    private static final String GROUP_ATTRIBUTE = "GROUP";

    private static final String HIERARCHY_LEVELS[] = {"level-0", "level-1", "level-2", "level-3", "level-4", "level-5",
            "level-6", "level-7", "level-8"};

    private static final String GROUP_SEPARATOR = "#";

    @Autowired
    CSVUtility utilCSV;

    @Autowired
    MovilizerClient movilizer;

    @Autowired
    OXMUtility utilityOXM;

    @Value("${assignmentconfig.dir}")
    private String configFilesDir;

    @Value("${movilizer.systemId}")
    private String systemID;


    /**
     * Delete Participant Configuration attribute MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY for all users.
     * This method assign a movelet that should exist
     *
     * @param moveletKey which triggers MAF script
     * @param createMovelet - if movelet needs to be created first
     * @param moveletPath - movelet path
     * @param participantsToDelete - how many participants need to delete
     * @param minsBeetwenExecutions - recommended 3-5 mins. This method will trigger script several times.
     * @param participantsDeletedEachExecution - number of participant configurations deleted each execution
     * @throws InterruptedException
     */
    public void deleteAllParticipantConfigurationTriggeringMAFScript(String moveletKey, String moveletPath, boolean createMovelet, int participantsToDelete, int minsBeetwenExecutions, int participantsDeletedEachExecution) throws InterruptedException {
        MovilizerParticipant participant = new MovilizerParticipant();
        participant.setName("deleteAll");
        participant.setDeviceAddress("@deleteAll@movilizer-bpost.com");
        participant.setParticipantKey("deleteAll");

        if(createMovelet){
            MovilizerMovelet movelet = movilizer.unmarshallMoveletFromFile(moveletPath);
            movelet.setMoveletKey(moveletKey);
            movilizer.createMovelet(movelet);
        }

        int numberOfExecutionsEachTriggered = 6;
        int timesNeededToDeleteAll = participantsToDelete / (participantsDeletedEachExecution*numberOfExecutionsEachTriggered);
        if(participantsToDelete % (participantsDeletedEachExecution*numberOfExecutionsEachTriggered) != 0){
            timesNeededToDeleteAll++;
        }

        for(int i = 0; i < timesNeededToDeleteAll; i++){
            //to trigger script
            movilizer.assignMoveletToParticipant(moveletKey, participant);
            movilizer.perfomSyncToCloud();
            //wait until next
            Thread.sleep(minsBeetwenExecutions * 60 * 1000);
        }

    }

    /**
     * Delete Participant Configuration attribute MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY for all users.
     * This method assign a movelet that should exist
     *
     * @param createMovelet - if movelet needs to be created first
     * @param participantsToDelete - how many participants need to delete
     * @throws InterruptedException
     */
    public void defaultDeleteAllParticipantConfigurationTriggeringMAFScript(boolean createMovelet, int participantsToDelete) throws InterruptedException {
        String moveletKey = "com.movilizer.bpost.deleteAllTrigger";
        deleteAllParticipantConfigurationTriggeringMAFScript(moveletKey, "/bpost/movelets/deleteAllConfig.mxml", createMovelet, participantsToDelete, 3, 100);
    }



        public void pushSotiCSV(boolean writeToXML, String hash) {

        Map<MovilizerParticipant, MovilizerParticipantConfiguration> configurations = createGroupParticipantsFromCSV(hash);
        if (writeToXML) {
            writeConfigurationDataToXMLFile(configurations);
        }
        movilizer.assignConfigurationToParticipant(configurations.values());
        movilizer.perfomSyncToCloud();
    }

    private void writeConfigurationDataToXMLFile(Map<MovilizerParticipant, MovilizerParticipantConfiguration> configurations) {

        String xml = "";
        for (Map.Entry<MovilizerParticipant, MovilizerParticipantConfiguration> participantEntry : configurations.entrySet()) {
            xml += utilityOXM.toString(new ObjectFactory()
                            .createMovilizerParticipantConfiguration(participantEntry
                                    .getValue()),
                    MovilizerParticipantConfiguration.class);
        }
        try {
            writeContentToFile(xml, new File("mov-logs/bpost", "request.xml"));
        } catch (IOException e) {
            logger.error("error while writing the gorup config to file", e);
        }
    }

    private void writeContentToFile(String content, File tFile)
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(tFile);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                fileOutputStream, "UTF8"));

        out.write(content);
        out.close();
        fileOutputStream.close();
    }


    private Map<MovilizerParticipant, MovilizerParticipantConfiguration> createGroupParticipantsFromCSV(String hash) {
        Map<MovilizerParticipant, MovilizerParticipantConfiguration> result = new HashMap<MovilizerParticipant, MovilizerParticipantConfiguration>();
        boolean first = true;
        for (Entry<String, String[]> entry : utilCSV.createMapFromCSV(configFilesDir + "import.csv").entrySet()) {
            String deviceAddress = entry.getKey();
            String name = getPrefixFromEmailDeviceAddress(deviceAddress);
            String participantKey = deviceAddress.replaceFirst("@", "").replace("+", "");

            MovilizerParticipant tMovilizerParticipant = new MovilizerParticipant();
            tMovilizerParticipant.setDeviceAddress(deviceAddress);
            tMovilizerParticipant.setName(name);
            tMovilizerParticipant.setParticipantKey(participantKey);

            MovilizerParticipantConfiguration movilizerParticipantConf = new MovilizerParticipantConfiguration();

            movilizerParticipantConf.setDeviceAddress(deviceAddress);
            movilizerParticipantConf.setPasswordHashType(PasswordTypes.PLAIN_TEXT_PASSWORD.getValue());
            movilizerParticipantConf.setPasswordHashValue(entry.getValue()[2]);
            movilizerParticipantConf.setName(name);

            List<Group> groups = createGroupsFromString(entry.getValue()[1], "/");
            movilizerParticipantConf.getAttributeUpdate().add(getParticipantAttributeUpdateFromGroupString(groups));
            //add hash code
            movilizerParticipantConf.getAttributeUpdate().add(getHashParticipantAttributeUpdate(hash));

			if(first)
			{
				movilizerParticipantConf.getGroupsReset().add(Long.parseLong(systemID));
				first = false;
			}

            for (Group group : groups) {
                MovilizerParticipantGroup participantGroup = new MovilizerParticipantGroup();
                participantGroup.setName(group.id);
                movilizerParticipantConf.getGroupAdd().add(participantGroup);
            }

            result.put(tMovilizerParticipant, movilizerParticipantConf);
        }
        return result;
    }


    private String getPrefixFromEmailDeviceAddress(String email) {
        String[] participant = email.split("@");
        return participant[1];
    }

    private List<Group> createGroupsFromString(String groupString, String separator) {
        List<Group> result = new ArrayList<Group>();
        String groupId = "";
        for (String currentGroup : groupString.split(separator)) {
            if (currentGroup.isEmpty() == false) {
                Group group = new Group();
                group.name = currentGroup;
                if (groupId.isEmpty() == false) {
                    groupId += GROUP_SEPARATOR;
                }
                groupId += currentGroup.replaceAll(" ", "");
                group.id = groupId;
                result.add(group);
            }
        }
        return result;
    }



    private MovilizerAttributeEntry getHashParticipantAttributeUpdate(String hash){
        MovilizerAttributeEntry hashAttribute = new MovilizerAttributeEntry();
        hashAttribute.setName(HASH_ATTRIBUTE_GROUP);
        MovilizerGenericDataContainerEntry hashEntry = new MovilizerGenericDataContainerEntry();
        hashEntry.setName(HASH_ATTRIBUTE);
        hashEntry.setValstr(hash);
        MovilizerGenericDataContainer container = new MovilizerGenericDataContainer();
        container.getEntry().add(hashEntry);
        hashAttribute.setEntry(container);

        return hashAttribute;
    }

    /**
     * <participantConfiguration deviceAddress="+9990001" > <attributeUpdate
     * name="MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY" useIndex="true"> <entry>
     * <entry name="COUNTRY"> <entry name="CITY"> <entry name="PART"> <entry
     * name="POSTAL_CODE"> <entry name="GROUP"> <valstr>de_MA_LI_68163</valstr>
     * </entry> <valstr>68163</valstr> </entry>
     * <entry name="GROUP"> <valstr>de_MA_LI</valstr> </entry>
     * <valstr>Lindenhof</valstr> </entry>
     * <entry name="GROUP"> <valstr>de_MA</valstr> </entry>
     * <valstr>Mannheim</valstr> </entry>
     * <entry name="GROUP"> <valstr>de</valstr> </entry>
     * <valstr>Germany</valstr> </entry> </entry> </attributeUpdate>
     * <attributeUpdate name="MOVILIZER_ASSIGNMENT_BOARD_GROUP" useIndex="true">
     * <entry> <entry name="NAME"> <valstr>Jamie
     * Random</valstr> </entry> </entry> </attributeUpdate>
     * <groupAdd name="de"/> <groupAdd name="de_MA"/> <groupAdd name=
     * "de_MA_LI"/> <groupAdd name="de_MA_LI_68163"/>
     * </participantConfiguration>
     *
     * @param groups
     */
    private MovilizerAttributeEntry getParticipantAttributeUpdateFromGroupString(List<Group> groups) {
        MovilizerAttributeEntry entry = new MovilizerAttributeEntry();
        entry.setName(HIERARCHY_ATTRIBUTE);
        entry.setUseIndex(true);
        MovilizerGenericDataContainer nextEntry = new MovilizerGenericDataContainer();
        entry.setEntry(nextEntry);
        MovilizerGenericDataContainerEntry currentEntry = null;

        int i = 0;
        for (Group currentGroup : groups) {
            if (currentEntry == null) {
                currentEntry = new MovilizerGenericDataContainerEntry();
                nextEntry.getEntry().add(currentEntry);
            } else {
                MovilizerGenericDataContainerEntry tempEntry = new MovilizerGenericDataContainerEntry();
                currentEntry.getEntry().add(tempEntry);
                currentEntry = tempEntry;
            }
            //TODO
            currentEntry.setName("level-" + i);
            currentEntry.setValstr(currentGroup.name);

            MovilizerGenericDataContainerEntry groupEntry = new MovilizerGenericDataContainerEntry();
            groupEntry.setName(GROUP_ATTRIBUTE);
            groupEntry.setValstr(currentGroup.id);
            currentEntry.getEntry().add(groupEntry);

            i++;
        }
        return entry;
    }

    private class Group {
        public String name;

        public String id;
    }
}
