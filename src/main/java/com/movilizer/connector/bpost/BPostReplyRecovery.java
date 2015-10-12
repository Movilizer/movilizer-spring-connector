package com.movilizer.connector.bpost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.movilitas.movilizer.v14.MovilizerReplyMovelet;
import com.movilizer.connector.service.OXMUtility;

public class BPostReplyRecovery {


	@Autowired
	OXMUtility utilityOXM;

	public void generateCSVFromReplysIntegratedApp() {
		List<MovilizerReplyMovelet> replies = getRepliesFromDir(
				"C:/work/workspace-tools/movilizer-spring-connector/src/test/resources/bpost/replies/accelerator_int");

		List<List<String>> csvResults = new ArrayList<List<String>>();
		for (MovilizerReplyMovelet replyMovelet : replies) {
			List<String> answerFields = new ArrayList<String>();
			answerFields.add(replyMovelet.getMoveletKey());
			answerFields.add("");
			answerFields.add(replyMovelet.getDeviceAddress());
			answerFields.add(replyMovelet.getCreationTimestamp().toString());
			createMultipleChoiceAnswer(answerFields, replyMovelet.getReplyQuestion().get(0).getReplyAnswer().get(0).getValue(),
					"P");
			answerFields.add(replyMovelet.getReplyQuestion().get(1).getReplyAnswer().get(0).getValue());
			answerFields.add(replyMovelet.getReplyQuestion().get(2).getReplyAnswer().get(0).getValue());
			answerFields.add(replyMovelet.getReplyQuestion().get(2).getReplyAnswer().get(1).getValue());
			answerFields.add(replyMovelet.getReplyQuestion().get(2).getReplyAnswer().get(2).getValue());
			answerFields.add(replyMovelet.getReplyQuestion().get(2).getReplyAnswer().get(3).getValue());
			csvResults.add(answerFields);
		}

		writeCSVFile(csvResults, "C:/Users/Pavel Kotlov/Google Drive/BPost/interiums-process/accelerator-int-results/results.csv");

	}

	public void generateCSVFromReplysApp() {
		List<MovilizerReplyMovelet> replies = getRepliesFromDir(
				"C:/work/workspace-tools/movilizer-spring-connector/src/test/resources/bpost/replies/accelerator");

		List<List<String>> csvResults = new ArrayList<List<String>>();
		for (MovilizerReplyMovelet replyMovelet : replies) {
			List<String> answerFields = new ArrayList<String>();
			answerFields.add(replyMovelet.getMoveletKey());
			answerFields.add("");
			answerFields.add(replyMovelet.getDeviceAddress());
			answerFields.add(replyMovelet.getCreationTimestamp().toString());
			String firstReply = replyMovelet.getReplyQuestion().get(0).getReplyAnswer().get(0).getValue();
			if(firstReply.startsWith("Dist"))
			{
				answerFields.add("x");
				answerFields.add("");
				answerFields.add("");
				answerFields.add("");

				addEmptyFields(answerFields, 1);
				addEmptyFields(answerFields, 3);
				addEmptyFields(answerFields, 14);
				addEmptyFields(answerFields, 4);
				addEmptyFields(answerFields, 2);
				addEmptyFields(answerFields, 2);

				answerFields.add(getValueQuestionAnswer(1, 0, replyMovelet));

				createBooleanAnswer(answerFields, getValueQuestionAnswer(2, 0, replyMovelet), "O");
				createMultipleChoiceAnswer(answerFields, getValueQuestionAnswer(3, 0, replyMovelet), "Ad", "N", "R");

				answerFields.add(getValueQuestionAnswer(4, 0, replyMovelet));

				answerFields.add(getValueQuestionAnswer(5, 0, replyMovelet));
				answerFields.add(getValueQuestionAnswer(5, 1, replyMovelet));
				answerFields.add(getValueQuestionAnswer(5, 2, replyMovelet));
			}
			else if(firstReply.startsWith("D"))
			{
				answerFields.add("");
				answerFields.add("x");
				answerFields.add("");
				answerFields.add("");

				addEmptyFields(answerFields, 1);
				answerFields.add(getValueQuestionAnswer(1, 0, replyMovelet));
				answerFields.add(getValueQuestionAnswer(1, 1, replyMovelet));
				answerFields.add(getValueQuestionAnswer(1, 2, replyMovelet));
				addEmptyFields(answerFields, 14);
				addEmptyFields(answerFields, 4);
				answerFields.add(getValueQuestionAnswer(2, 0, replyMovelet));
				answerFields.add(getValueQuestionAnswer(2, 1, replyMovelet));
				addEmptyFields(answerFields, 2);
				addEmptyFields(answerFields, 9);

			}
			else if(firstReply.startsWith("Pick"))
			{
				answerFields.add("");
				answerFields.add("");
				answerFields.add("x");
				answerFields.add("");

				addEmptyFields(answerFields, 1);
				addEmptyFields(answerFields, 3);
				addEmptyFields(answerFields, 14);
				addEmptyFields(answerFields, 4);
				addEmptyFields(answerFields, 2);
				addEmptyFields(answerFields, 2);
				addEmptyFields(answerFields, 9);
			}
			else if(firstReply.startsWith("Retour"))
			{
				answerFields.add("");
				answerFields.add("");
				answerFields.add("");
				answerFields.add("x");

				addEmptyFields(answerFields, 1);
				addEmptyFields(answerFields, 3);
				addEmptyFields(answerFields, 14);
				answerFields.add(getValueQuestionAnswer(1, 0, replyMovelet));
				answerFields.add(getValueQuestionAnswer(1, 1, replyMovelet));
				answerFields.add(getValueQuestionAnswer(1, 2, replyMovelet));
				answerFields.add(getValueQuestionAnswer(1, 3, replyMovelet));
				addEmptyFields(answerFields, 2);
				answerFields.add(getValueQuestionAnswer(2, 0, replyMovelet));
				answerFields.add(getValueQuestionAnswer(2, 1, replyMovelet));
				addEmptyFields(answerFields, 9);
			}
			csvResults.add(answerFields);
		}

		writeCSVFile(csvResults, "C:/Users/Pavel Kotlov/Google Drive/BPost/interiums-process/accelerator-results/results.csv");

	}

	private void createBooleanAnswer(List<String> answerFields, String value, String prefix) {
		if (value.startsWith(prefix)) {
			answerFields.add("x");
		} else {
			answerFields.add("");
		}

	}

	private String getValueQuestionAnswer(int question, int answer, MovilizerReplyMovelet replyMovelet) {
		return replyMovelet.getReplyQuestion().get(question).getReplyAnswer().get(answer).getValue();
	}

	private void addEmptyFields(List<String> answerFields, int number) {
		for(int i = 0; i < number; i++)
		{
			answerFields.add("");
		}
	}

	private void writeCSVFile(List<List<String>> csvResults, String path) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(path));
			for (List<String> line : csvResults) {
				StringBuilder sb = new StringBuilder();
				for (String value : line) {
					sb.append(value);
					sb.append(",");
				}
				br.write(sb.toString());
				br.newLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createMultipleChoiceAnswer(List<String> answerFields, String value, String prefix) {
		if (value.startsWith(prefix)) {
			answerFields.add("x");
			answerFields.add("");
		} else {
			answerFields.add("");
			answerFields.add("x");
		}
	}

	private void createMultipleChoiceAnswer(List<String> answerFields, String value, String prefix1, String prefix2, String prefix3) {
		if (value.startsWith(prefix1)) {
			answerFields.add("x");
			answerFields.add("");
			answerFields.add("");
			answerFields.add("");
		} else if(value.startsWith(prefix2)) {
			answerFields.add("");
			answerFields.add("x");
			answerFields.add("");
			answerFields.add("");
		} else if(value.startsWith(prefix3)) {
			answerFields.add("");
			answerFields.add("");
			answerFields.add("x");
			answerFields.add("");
		} else {
			answerFields.add("");
			answerFields.add("");
			answerFields.add("");
			answerFields.add("x");
		}
	}

	private List<MovilizerReplyMovelet> getRepliesFromDir(String path) {
		List<MovilizerReplyMovelet> result = new ArrayList<MovilizerReplyMovelet>();
		File root = new File(path);
		System.out.println(root.isDirectory());
		File[] list = root.listFiles();
		if (list == null)
			return result;
		for (File file : list) {
			try {
				result.add(utilityOXM.deserialize(file.toURL(), MovilizerReplyMovelet.class));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
