using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Fantacy_Model
{
    public class WalletModel
    {
        public string Name { get; set; }
        public string UserTier { get; set; }
        public string TierExpiryDate { get; set; }
        public string WalletPoints { get; set; }
        public string TournamentTotal { get; set; }
        public string MatchContestTotal { get; set; }
        public string SignUpDate { get; set; }
        public string TierStartDate { get; set; }
        public string RetainTier { get; set; }
        public string RetainTierByPoints { get; set; }
        public string RetainTierByDate { get; set; }
        public string AttainTier { get; set; }
        public string AttainTierByPoints { get; set; }
        public string AttainTierByDate { get; set; }
       
    }

    public class WalletModelResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<WalletModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TotalClaimModelResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TotalClaimModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TotalClaimModel
    {
        public string TotalClaimAmount { get; set; }
        public string ClaimId { get; set; }
        public string ClaimDate { get; set; }
        public string ClaimAmount { get; set; }
        public string Bundle { get; set; }
        public string Vouchar { get; set; }
        public string TotalOutstandingAmount { get; set; }
        public string TotalCash { get; set; }
        public string MinAmountToWithDraw { get; set; }
    }

    public class TotalRewardModelResponse
    {
        public string status { get; set; }
        public string statusMessage { get; set; }
        public List<TotalRewardModel> data { get; set; }
        public string httpStatus { get; set; }
        public string httpStatusCode { get; set; }
        public string httpStatusDescription { get; set; }

    }
    public class TotalRewardModel
    {
        public string TotalRewardAmount { get; set; }
        public string RewardId { get; set; }
        public string RewardDate { get; set; }
        public string RewardType { get; set; }
        public string RewardAmount { get; set; }
        public string Details { get; set; }

    }
}
