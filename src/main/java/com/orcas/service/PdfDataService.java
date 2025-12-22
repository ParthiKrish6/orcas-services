package com.orcas.service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.orcas.entity.BattingDetails;
import com.orcas.entity.BowlingDetails;
import com.orcas.entity.FieldingDetails;
import com.orcas.entity.MatchDetails;
import com.orcas.entity.PlayerDetails;
import com.orcas.entity.TeamDetails;
import com.orcas.repository.BattingDetailsRepository;
import com.orcas.repository.BowlingDetailsRepository;
import com.orcas.repository.FieldingDetailsRepository;
import com.orcas.repository.MatchDetailsRepository;
import com.orcas.repository.PlayerDetailsRepository;
import com.orcas.repository.TeamDetailsRepository;

@Service
public class PdfDataService {

	private final String UPLOAD_DIR = "uploads/";

	@Autowired
	private MatchDetailsRepository matchDetailsRepository;

	@Autowired
	private BattingDetailsRepository battingDetailsRepository;

	@Autowired
	private BowlingDetailsRepository bowlingDetailsRepository;

	@Autowired
	private TeamDetailsRepository teamDetailsRepository;

	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;
	
	@Autowired
	private FieldingDetailsRepository fieldingDetailsRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void readPdfFile(MultipartFile file) throws FileAlreadyExistsException, IOException {
		Path filePath = null;
		try {
			MatchDetails matchDetails;
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			filePath = uploadPath.resolve(file.getOriginalFilename());
			Files.copy(file.getInputStream(), filePath);
			PDDocument document = PDDocument.load(filePath.toFile());
			String page1 = "";
			String page3 = "";
			String page4 = "";
			for (int i = 1; i <= document.getNumberOfPages(); i++) {
				PDFTextStripper pdfStripper = new PDFTextStripper();
				pdfStripper.setStartPage(i); // Set the start page for extraction
				pdfStripper.setEndPage(i); // Set the end page for extraction (same as start for single page)

				String pageText = pdfStripper.getText(document);
				if (i == 1) {
					page1 = pageText;
				} else if (i == 3) {
					page3 = pageText;
				} else if (i == 4) {
					page4 = pageText;
				} 
			}
			document.close();

			List<TeamDetails> teams = teamDetailsRepository.findAll();
			List<PlayerDetails> players = playerDetailsRepository.findAll();

			String[] details = page1.split("\n");
			int startIndex = 0;
			for(String st : details) {
				startIndex++;
				if(st.contains("Match Details")) {
					break;
				}
			}
			
			matchDetails = insertMatchDetails(page1, page3, teams, startIndex, details);
			
			if ("Y".equals(matchDetails.getBatFirst())) {
				List<String> playerList = getPlayers(page3);
				insertBattingDetails(page3, matchDetails, players, playerList);
				insertBowlingDetails(page4, matchDetails, players, playerList);
				insertFieldingDetails(page4, matchDetails, players, playerList);
			} else {
				List<String> playerList = getPlayers(page4);
				insertBowlingDetails(page3, matchDetails, players, playerList);
				insertBattingDetails(page4, matchDetails, players, playerList);
				insertFieldingDetails(page3, matchDetails, players, playerList);
			}

		} catch (FileAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			Files.deleteIfExists(filePath);
		}
	}
	
	private List<String> getPlayers(String pageText) {
		List<String> playerList = new ArrayList<String>();
		String[] details = pageText.split("\n");
		int startIndex = 0;
		int endIndex = 0;
		String dnbPlayers = "";
		for (int i = 0; i < details.length; i++) {
			if (details[i].contains("No Batsman")) {
				startIndex = i;
			} else if (details[i].contains("Extras: ")) {
				endIndex = i - 1;
			} else if (details[i].contains("To Bat: ")) {
				dnbPlayers = details[i].substring(details[i].indexOf("To Bat: ")+8);
			}
		}
		for (int j = startIndex+1; j < endIndex+1; j++) {
			String removedNumber = trimReplaceSpl(details[j].substring(2));
			String playerName = removedNumber.substring(0, removedNumber.indexOf(" ("));
			playerList.add(playerName);
		}
		if(!dnbPlayers.trim().isEmpty()) {
			for(String dnbPlayer : dnbPlayers.split(",")) {
				playerList.add(dnbPlayer.trim());
			}
		}
		return playerList;
	}

	private static String trimReplaceSpl(String st) {
		return st.replace("\r", "").replace("\n", "").replace("†", "").trim();
	}

	public MatchDetails insertMatchDetails(String pageText, String page3, List<TeamDetails> teams, int startIndex, String[] details) throws IOException {
		MatchDetails matchDetails = new MatchDetails();
		
		String myTeam = trimReplaceSpl(details[startIndex+1]);
		String oppTeam = trimReplaceSpl(details[startIndex+1]);
		String team1 = trimReplaceSpl(details[startIndex].replace("Match ", "").replace(" vs", ""));
		boolean found = teams.stream().anyMatch(obj -> obj.getTeamName().equals(team1));
		if (found) {
			myTeam = team1;
		} else {
			oppTeam = team1;
		}
		
		
		System.out.println("My Team ::: "+myTeam);
		System.out.println("Opp Team ::: "+oppTeam);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(details[startIndex+4].replace("Date ", "").substring(0, 10), formatter);

		String result = details[startIndex+9].replace("Result ", "");
		if (result.contains(myTeam)) {
			matchDetails.setMatchResult("WON");
		} else {
			matchDetails.setMatchResult("LOST");
		}

		String scoreTeam1 = details[startIndex+7].replace("Total ", "");
		String scoreTeam2 = details[startIndex+7].replace("Total ", "");
		if (scoreTeam1.contains(myTeam)) {
			scoreTeam2 = details[startIndex+8];

		} else {
			scoreTeam1 = details[startIndex+8];
		}

		matchDetails.setMargin(result.substring(result.indexOf("won by ") + 7).replace("\n", "").replace("\r", ""));
		matchDetails.setMatchDate(date);
		matchDetails.setOpponent(oppTeam.replace("\n", "").replace("\r", ""));
		matchDetails.setOpponentScore(
				scoreTeam2.substring(findFirstDigitPosition(scoreTeam2)).replace("\n", "").replace("\r", ""));
		TeamDetails team = teamDetailsRepository.getTeamFromName(myTeam.replace("\n", "").replace("\r", ""));
		if (team == null) {
			throw new IOException("Team Not Found");
		}
		matchDetails.setTeamDetails(team);

		matchDetails.setTeamDetails(team);
		matchDetails.setTeamScore(
				scoreTeam1.substring(findFirstDigitPosition(scoreTeam1)).replace("\n", "").replace("\r", ""));
		
		details = page3.split("\n");
		String battingFirstTeam = null;
		for(String st : details) {
			startIndex++;
			if(st.contains("(1st Innings)")) {
				battingFirstTeam = st;
				break;
			}
		}
		matchDetails.setBatFirst("N");
		if(trimReplaceSpl(battingFirstTeam.substring(0, battingFirstTeam.indexOf("(1st Innings)"))).contains(matchDetails.getTeamDetails().getTeamName())) {
			matchDetails.setBatFirst("Y");
		}
		
		return matchDetailsRepository.save(matchDetails);
	}

	public void insertBattingDetails(String pageText, MatchDetails matchDetails, List<PlayerDetails> players, List<String> playedList)
			throws IOException {
		List<BattingDetails> battingDetails = new ArrayList<BattingDetails>();
		String[] details = pageText.split("\n");
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < details.length; i++) {
			if (details[i].contains("No Batsman")) {
				startIndex = i;
			} else if (details[i].contains("Extras: ")) {
				endIndex = i - 1;
			}
		}
		List<String> addedPlayers = new ArrayList<String>();
		for (int j = startIndex+1; j < endIndex+1; j++) {
			String removedNumber = trimReplaceSpl(details[j].substring(2));
			System.out.println("Batting ::: Processing ::: "+removedNumber);
			String[] content = removedNumber.split(" ");
			String playerName = removedNumber.substring(0, removedNumber.indexOf(" ("));
			BattingDetails batting = new BattingDetails();
			batting.setStrikeRate(content[content.length-1]);
			batting.setSixes(content[content.length-2]);
			batting.setFours(content[content.length-3]);
			batting.setTimeSpent(content[content.length-4]);
			batting.setBalls(content[content.length-5]);
			batting.setRuns(content[content.length-6]);
			batting.setNotOut(details[j].contains("not out") ? "Y" : "N");
			batting.setMatchDetails(matchDetails);
			
			List<PlayerDetails> filteredObjects = players.stream()
			            .filter(obj -> (playerName.equalsIgnoreCase(obj.getPlayerName()) || playerName.equalsIgnoreCase(obj.getNickName())))
			            .collect(Collectors.toList());
	        
	        if(filteredObjects.size() == 0) {
	        	throw new IOException("Player Not Found :: "+playerName);
	        } else if(filteredObjects.size() > 1) {
	        	throw new IOException("More than One Player Found :: "+playerName);
	        }
	        
			batting.setPlayerDetails(filteredObjects.get(0));
			battingDetails.add(batting);
			addedPlayers.add(playerName);
		}
		
		for(String played : playedList) {
			if(!addedPlayers.stream().filter(o -> o.trim().equalsIgnoreCase(played.trim())).findAny().isPresent()) {
				BattingDetails batting = new BattingDetails();
				batting.setStrikeRate("DNB");
				batting.setSixes("DNB");
				batting.setFours("DNB");
				batting.setTimeSpent("DNB");
				batting.setBalls("DNB");
				batting.setRuns("DNB");
				batting.setNotOut("DNB");
				batting.setMatchDetails(matchDetails);
				
				List<PlayerDetails> filteredObjects = players.stream()
				            .filter(obj -> (played.equalsIgnoreCase(obj.getPlayerName()) || played.equalsIgnoreCase(obj.getNickName())))
				            .collect(Collectors.toList());
		        
		        if(filteredObjects.size() == 0) {
		        	throw new IOException("Player Not Found :: "+played);
		        } else if(filteredObjects.size() > 1) {
		        	throw new IOException("More than One Player Found :: "+played);
		        }
		        
				batting.setPlayerDetails(filteredObjects.get(0));
				battingDetails.add(batting);
			}
		}

		battingDetailsRepository.saveAll(battingDetails);
	}

	public void insertBowlingDetails(String pageText, MatchDetails matchDetails, List<PlayerDetails> players, List<String> playedList)
			throws IOException {

		List<BowlingDetails> bowlingDetails = new ArrayList<BowlingDetails>();
		String[] details = pageText.split("\n");
		int startIndex = 0;
		int endIndex = details.length;
		for (int i = 0; i < details.length; i++) {
			if (trimReplaceSpl(details[i]).contains("No Bowler")) {
				startIndex = i;
			}
		}

		List<String> addedPlayers = new ArrayList<String>();
		for (int j = startIndex+1; j < endIndex; j++) {
			if(details[j] != null && !trimReplaceSpl(details[j]).isEmpty()) {
				String removedNumber = trimReplaceSpl(details[j]).substring(2);
				System.out.println("Bowling ::: Processing ::: "+removedNumber);
				String[] content = removedNumber.split(" ");
				String playerName = removedNumber.substring(0, removedNumber.indexOf(content[content.length-10])).replace("(c)", "").trim();
				
				BowlingDetails bowling = new BowlingDetails();
				bowling.setEconomy(content[content.length-1]);
				bowling.setNoballs(content[content.length-2]);
				bowling.setWides(content[content.length-3]);
				bowling.setSixes(content[content.length-4]);
				bowling.setFours(content[content.length-5]);
				bowling.setDots(content[content.length-6]);
				bowling.setWickets(content[content.length-7]);
				bowling.setRuns(content[content.length-8]);
				bowling.setMaidens(content[content.length-9]);
				bowling.setOvers(content[content.length-10]);
				bowling.setMatchDetails(matchDetails);
				
				List<PlayerDetails> filteredObjects = players.stream()
				            .filter(obj -> (playerName.equalsIgnoreCase(obj.getPlayerName()) || playerName.equalsIgnoreCase(obj.getNickName())))
				            .collect(Collectors.toList());
		        
		        if(filteredObjects.size() == 0) {
		        	throw new IOException("Player Not Found :: "+playerName);
		        } else if(filteredObjects.size() > 1) {
		        	throw new IOException("More than One Player Found :: "+playerName);
		        }
		        
				bowling.setPlayerDetails(filteredObjects.get(0));
				bowlingDetails.add(bowling);
				addedPlayers.add(playerName);
			}
		}
		
		for(String played : playedList) {
			if(!addedPlayers.stream().filter(o -> o.trim().equalsIgnoreCase(played.trim())).findAny().isPresent()) {
				BowlingDetails bowling = new BowlingDetails();
				bowling.setEconomy("DNB");
				bowling.setNoballs("DNB");
				bowling.setWides("DNB");
				bowling.setSixes("DNB");
				bowling.setFours("DNB");
				bowling.setDots("DNB");
				bowling.setWickets("DNB");
				bowling.setRuns("DNB");
				bowling.setMaidens("DNB");
				bowling.setOvers("DNB");
				bowling.setMatchDetails(matchDetails);
				
				List<PlayerDetails> filteredObjects = players.stream()
				            .filter(obj -> (played.equalsIgnoreCase(obj.getPlayerName()) || played.equalsIgnoreCase(obj.getNickName())))
				            .collect(Collectors.toList());
		        
		        if(filteredObjects.size() == 0) {
		        	throw new IOException("Player Not Found :: "+played);
		        } else if(filteredObjects.size() > 1) {
		        	throw new IOException("More than One Player Found :: "+played);
		        }
		        
				bowling.setPlayerDetails(filteredObjects.get(0));
				bowlingDetails.add(bowling);
			}
		}
		
		bowlingDetailsRepository.saveAll(bowlingDetails);
	}
	
	
	public void insertFieldingDetails(String pageText, MatchDetails matchDetails, List<PlayerDetails> players, List<String> playedList)
			throws IOException {
		List<FieldingDetails> fieldingDetails = new ArrayList<FieldingDetails>();
		String[] details = pageText.split("\n");
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < details.length; i++) {
			if (details[i].contains("No Batsman")) {
				startIndex = i;
			} else if (details[i].contains("Extras: ")) {
				endIndex = i - 1;
			}
		}
		Map<String, FieldingDetails> fieldingMap = new HashMap<String, FieldingDetails>();
		for (int j = startIndex+1; j < endIndex+1; j++) {
			String removedNumber = trimReplaceSpl(details[j]).substring(2);
			System.out.println("Fielding ::: Processing ::: "+removedNumber);
			String[] content = removedNumber.split(" ");
			String fieldDet = removedNumber.substring(removedNumber.indexOf("HB)")+3, removedNumber.indexOf(" " +content[content.length-6] +" ")).trim();
			String runOut[] = null;
			System.out.println(fieldDet);
			if(fieldDet.contains("c ") && fieldDet.contains(" b ")) {
				String catcher = trimReplaceSpl(fieldDet.substring(fieldDet.indexOf("c ")+2, fieldDet.indexOf(" b ")));
				setFieldingMap(fieldingMap, catcher, players, matchDetails, "C");
			} else if(fieldDet.contains("c&b")) {
				String catcher = trimReplaceSpl(fieldDet.substring(fieldDet.indexOf("c&b")+3));
				setFieldingMap(fieldingMap, catcher, players, matchDetails, "C");
			} else if(fieldDet.contains("run out ")) {
				runOut = fieldDet.substring(fieldDet.indexOf("run out ")+8).trim().split("/");
				for(String player : runOut) {
					player = trimReplaceSpl(player);
					setFieldingMap(fieldingMap, player, players, matchDetails, "F"); 
				}
			}
		}
		fieldingDetails = new ArrayList<>(fieldingMap.values());
		
		for(String played : playedList) {
			if(!fieldingMap.keySet().stream().filter(o -> o.trim().equalsIgnoreCase(played.trim())).findAny().isPresent()) {
				FieldingDetails fielding = new FieldingDetails();
				fielding.setCatches("0");
				fielding.setCatchesDropped("0");
				fielding.setRunOuts("0");
				fielding.setRunsMissed("0");
				fielding.setRunsSaved("0");
				fielding.setMatchDetails(matchDetails);
				
				List<PlayerDetails> filteredObjects = players.stream()
				            .filter(obj -> (played.equalsIgnoreCase(obj.getPlayerName()) || played.equalsIgnoreCase(obj.getNickName())))
				            .collect(Collectors.toList());
		        
		        if(filteredObjects.size() == 0) {
		        	throw new IOException("Player Not Found :: "+played);
		        } else if(filteredObjects.size() > 1) {
		        	throw new IOException("More than One Player Found :: "+played);
		        }
		        fielding.setPlayerDetails(filteredObjects.get(0));
		        fieldingDetails.add(fielding);
			}
		}
		
		fieldingDetailsRepository.saveAll(fieldingDetails);
	}

	private void setFieldingMap(Map<String, FieldingDetails> fieldingMap, String catcher, List<PlayerDetails> players, MatchDetails matchDetails, String type) throws IOException {
		List<PlayerDetails> filteredObjects = players.stream()
				.filter(obj -> (catcher.equalsIgnoreCase(obj.getPlayerName())
						|| catcher.equalsIgnoreCase(obj.getNickName())))
				.collect(Collectors.toList());

		if (filteredObjects.size() == 0) {
			throw new IOException("Player Not Found :: " + catcher);
		} else if (filteredObjects.size() > 1) {
			throw new IOException("More than One Player Found :: " + catcher);
		}

		if(fieldingMap.containsKey(catcher)) {
			FieldingDetails fielding = fieldingMap.get(catcher);
			if("F".equals(type)) {
				fielding.setRunOuts(String.valueOf(Integer.parseInt(fielding.getRunOuts())+1));
			} else if("C".equals(type)) {
				fielding.setCatches(String.valueOf(Integer.parseInt(fielding.getCatches())+1));
			}
		} else {
			FieldingDetails fielding = new FieldingDetails();
			fielding.setCatches("0");
			fielding.setCatchesDropped("0");
			fielding.setRunOuts("0");
			fielding.setRunsMissed("0");
			fielding.setRunsSaved("0");
			fielding.setMatchDetails(matchDetails);
			fielding.setPlayerDetails(filteredObjects.get(0));
			if("F".equals(type)) {
				fielding.setRunOuts(String.valueOf(Integer.parseInt(fielding.getRunOuts())+1));
			} else if("C".equals(type)) {
				fielding.setCatches(String.valueOf(Integer.parseInt(fielding.getCatches())+1));
			}
			fieldingMap.put(catcher, fielding);
		}
	}

	public static int findFirstDigitPosition(String str) {
		if (str == null || str.isEmpty()) {
			return -1; // Handle null or empty strings
		}

		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				return i; // Return the index of the first digit found
			}
		}
		return -1; // No digit found in the string
	}
}