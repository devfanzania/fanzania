
import Foundation
 
public class PlayerProfileResponseModel {
	public var tournamentId : Int?
	public var tournamentName : String?
	public var playerName : String?
	public var teamShortName : String?
	public var playerSpeciality : String?
	public var playerValue : Int?
	public var playerTotalPoints : Int?
	public var playerRank : Int?
	public var selectedBy : Int?
	public var playerPoints1 : String?
	public var playerPoints2 : String?
	public var playerPoints3 : String?
	public var playerPoints4 : String?
	public var playerPoints5 : String?
	public var playerRuns1 : String?
	public var playerRuns2 : String?
	public var playerRuns3 : String?
	public var playerRuns4 : String?
	public var playerRuns5 : String?
	public var playerWickets1 : String?
	public var playerWickets2 : String?
	public var playerWickets3 : String?
	public var playerWickets4 : String?
	public var playerWickets5 : String?
	public var playerValueRank : Int?
	public var totalPlayers : Int?
	public var imageURL : String?
	public var matchCounter : Int?
	public var lastMatchId : Int?

/**
    Returns an array of models based on given dictionary.
    
    Sample usage:
    let json4Swift_Base_list = Json4Swift_Base.modelsFromDictionaryArray(someDictionaryArrayFromJSON)

    - parameter array:  NSArray from JSON dictionary.

    - returns: Array of Json4Swift_Base Instances.
*/
    public class func modelsFromDictionaryArray(array:NSArray) -> [PlayerProfileResponseModel]
    {
        var models:[PlayerProfileResponseModel] = []
        for item in array
        {
            models.append(PlayerProfileResponseModel(dictionary: item as! NSDictionary)!)
        }
        return models
    }

/**
    Constructs the object based on the given dictionary.
    
    Sample usage:
    let json4Swift_Base = Json4Swift_Base(someDictionaryFromJSON)

    - parameter dictionary:  NSDictionary from JSON.

    - returns: Json4Swift_Base Instance.
*/
	required public init?(dictionary: NSDictionary) {

		tournamentId = dictionary["tournamentId"] as? Int
		tournamentName = dictionary["tournamentName"] as? String
		playerName = dictionary["playerName"] as? String
		teamShortName = dictionary["teamShortName"] as? String
		playerSpeciality = dictionary["PlayerSpeciality"] as? String
		playerValue = dictionary["playerValue"] as? Int
		playerTotalPoints = dictionary["playerTotalPoints"] as? Int
		playerRank = dictionary["playerRank"] as? Int
		selectedBy = dictionary["selectedBy"] as? Int
		playerPoints1 = dictionary["playerPoints1"] as? String
		playerPoints2 = dictionary["playerPoints2"] as? String
		playerPoints3 = dictionary["playerPoints3"] as? String
		playerPoints4 = dictionary["playerPoints4"] as? String
		playerPoints5 = dictionary["playerPoints5"] as? String
		playerRuns1 = dictionary["playerRuns1"] as? String
		playerRuns2 = dictionary["playerRuns2"] as? String
		playerRuns3 = dictionary["playerRuns3"] as? String
		playerRuns4 = dictionary["playerRuns4"] as? String
		playerRuns5 = dictionary["playerRuns5"] as? String
		playerWickets1 = dictionary["playerWickets1"] as? String
		playerWickets2 = dictionary["playerWickets2"] as? String
		playerWickets3 = dictionary["playerWickets3"] as? String
		playerWickets4 = dictionary["playerWickets4"] as? String
		playerWickets5 = dictionary["playerWickets5"] as? String
		playerValueRank = dictionary["playerValueRank"] as? Int
		totalPlayers = dictionary["totalPlayers"] as? Int
		imageURL = dictionary["imageURL"] as? String
		matchCounter = dictionary["MatchCounter"] as? Int
		lastMatchId = dictionary["LastMatchId"] as? Int
	}

		
/**
    Returns the dictionary representation for the current instance.
    
    - returns: NSDictionary.
*/
	public func dictionaryRepresentation() -> NSDictionary {

		let dictionary = NSMutableDictionary()

		dictionary.setValue(self.tournamentId, forKey: "tournamentId")
		dictionary.setValue(self.tournamentName, forKey: "tournamentName")
		dictionary.setValue(self.playerName, forKey: "playerName")
		dictionary.setValue(self.teamShortName, forKey: "teamShortName")
		dictionary.setValue(self.playerSpeciality, forKey: "PlayerSpeciality")
		dictionary.setValue(self.playerValue, forKey: "playerValue")
		dictionary.setValue(self.playerTotalPoints, forKey: "playerTotalPoints")
		dictionary.setValue(self.playerRank, forKey: "playerRank")
		dictionary.setValue(self.selectedBy, forKey: "selectedBy")
		dictionary.setValue(self.playerPoints1, forKey: "playerPoints1")
		dictionary.setValue(self.playerPoints2, forKey: "playerPoints2")
		dictionary.setValue(self.playerPoints3, forKey: "playerPoints3")
		dictionary.setValue(self.playerPoints4, forKey: "playerPoints4")
		dictionary.setValue(self.playerPoints5, forKey: "playerPoints5")
		dictionary.setValue(self.playerRuns1, forKey: "playerRuns1")
		dictionary.setValue(self.playerRuns2, forKey: "playerRuns2")
		dictionary.setValue(self.playerRuns3, forKey: "playerRuns3")
		dictionary.setValue(self.playerRuns4, forKey: "playerRuns4")
		dictionary.setValue(self.playerRuns5, forKey: "playerRuns5")
		dictionary.setValue(self.playerWickets1, forKey: "playerWickets1")
		dictionary.setValue(self.playerWickets2, forKey: "playerWickets2")
		dictionary.setValue(self.playerWickets3, forKey: "playerWickets3")
		dictionary.setValue(self.playerWickets4, forKey: "playerWickets4")
		dictionary.setValue(self.playerWickets5, forKey: "playerWickets5")
		dictionary.setValue(self.playerValueRank, forKey: "playerValueRank")
		dictionary.setValue(self.totalPlayers, forKey: "totalPlayers")
		dictionary.setValue(self.imageURL, forKey: "imageURL")
		dictionary.setValue(self.matchCounter, forKey: "MatchCounter")
		dictionary.setValue(self.lastMatchId, forKey: "LastMatchId")

		return dictionary
	}

}
