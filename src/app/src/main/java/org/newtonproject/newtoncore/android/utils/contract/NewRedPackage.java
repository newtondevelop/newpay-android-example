package org.newtonproject.newtoncore.android.utils.contract;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple12;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class NewRedPackage extends Contract {
    private static final String BINARY = "608060405262015180600355600a6004556002600555670de0b6b3a7640000600655336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550343373ffffffffffffffffffffffffffffffffffffffff167f7067f915d1c61a35abf2d3154544a7d09111584b648bdef0826a3f4f9a19f11a60405160405180910390a361279e806100b46000396000f3fe6080604052600436106100d5576000357c010000000000000000000000000000000000000000000000000000000090048063092a5cce1461011b578063096a8ab7146101255780630eb2e54c1461015357806330fc4cff146102cf57806336e3a811146102fd578063715018a61461033e5780638da5cb5b14610355578063933fbcef146103ac5780639ae4bf0e14610424578063aa4abe7f14610452578063c3b1d52b14610480578063df81620614610515578063e1acdb06146105bb578063f2fde38b1461063d578063f4f4bd9b1461068e575b343373ffffffffffffffffffffffffffffffffffffffff167f6245adff694c328e4626112edb5b6a84d3ff4dfd36ed631e24f77f64f8efcd4160405160405180910390a3005b6101236107b7565b005b6101516004803603602081101561013b57600080fd5b810190808035906020019092919050505061084c565b005b34801561015f57600080fd5b5061019f6004803603602081101561017657600080fd5b8101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506108be565b604051808d1515151581526020018c6fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020018b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018a8152602001891515151581526020018060200188815260200187600181111561023157fe5b60ff168152602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b8381101561028957808201518184015260208101905061026e565b50505050905090810190601f1680156102b65780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390f35b6102fb600480360360208110156102e557600080fd5b8101908080359060200190929190505050610a1a565b005b61033c6004803603602081101561031357600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190505050610a8c565b005b34801561034a57600080fd5b50610353610fc8565b005b34801561036157600080fd5b5061036a6110ca565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156103b857600080fd5b50610422600480360360608110156103cf57600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506110ef565b005b6104506004803603602081101561043a57600080fd5b8101908080359060200190929190505050611b7c565b005b61047e6004803603602081101561046857600080fd5b8101908080359060200190929190505050611bee565b005b34801561048c57600080fd5b506104d9600480360360408110156104a357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611c60565b60405180826fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200191505060405180910390f35b34801561052157600080fd5b506105646004803603602081101561053857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611cb5565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156105a757808201518184015260208101905061058c565b505050509050019250505060405180910390f35b3480156105c757600080fd5b50610627600480360360408110156105de57600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611d92565b6040518082815260200191505060405180910390f35b34801561064957600080fd5b5061068c6004803603602081101561066057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611eca565b005b61077b600480360360808110156106a457600080fd5b81019080803590602001906401000000008111156106c157600080fd5b8201836020820111156106d357600080fd5b803590602001918460018302840111640100000000831117156106f557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190803560ff16906020019092919080356fffffffffffffffffffffffffffffffff19169060200190929190505050611f31565b60405180826fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561081257600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16ff5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156108a757600080fd5b600081101515156108b457fe5b8060068190555050565b60026020528060005260406000206000915090508060000160009054906101000a900460ff16908060000160019054906101000a900470010000000000000000000000000000000002908060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060020154908060030160009054906101000a900460ff1690806004018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109df5780601f106109b4576101008083540402835291602001916109df565b820191906000526020600020905b8154815290600101906020018083116109c257829003601f168201915b5050505050908060050154908060060160009054906101000a900460ff169080600701549080600801549080600901549080600a015490508c565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610a7557600080fd5b60008110151515610a8257fe5b8060038190555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610ae757600080fd5b6001151560026000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060000160009054906101000a900460ff161515141515610bac576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f312e20496e76616c69642067696674496400000000000000000000000000000081525060200191505060405180910390fd5b610bb4612633565b60026000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002061018060405190810160405290816000820160009054906101000a900460ff161515151581526020016000820160019054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff16151515158152602001600482018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610d765780601f10610d4b57610100808354040283529160200191610d76565b820191906000526020600020905b815481529060010190602001808311610d5957829003601f168201915b50505050508152602001600582015481526020016006820160009054906101000a900460ff166001811115610da757fe5b6001811115610db257fe5b8152602001600782015481526020016008820154815260200160098201548152602001600a8201548152505090506001151581600001511515141515610df457fe5b600081604001519050816060015142111515610e9e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001807f362e204578706972792074696d6520697320626967676572207468616e206e6f81526020017f770000000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166108fc8361016001519081150290604051600060405180830381858888f19350505050158015610ee9573d6000803e3d6000fd5b50600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600a0181905550600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060080181905550600160026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060030160006101000a81548160ff021916908315150217905550505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561102357600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167ff8df31144d9c2f0f6b59d69b8b98abd5459d07f2742c4df920b25aae33c6482060405160405180910390a260008060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561114a57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415151561118357fe5b6113b560026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002061018060405190810160405290816000820160009054906101000a900460ff161515151581526020016000820160019054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff16151515158152602001600482018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156113485780601f1061131d57610100808354040283529160200191611348565b820191906000526020600020905b81548152906001019060200180831161132b57829003601f168201915b50505050508152602001600582015481526020016006820160009054906101000a900460ff16600181111561137957fe5b600181111561138457fe5b8152602001600782015481526020016008820154815260200160098201548152602001600a820154815250506124ca565b1515611429576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f312e20496e76616c69642067696674496400000000000000000000000000000081525060200191505060405180910390fd5b600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600b0160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054141515611540576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f322e20526563656970742061646472657373206861732072656365697074656481526020017f207468652067696674000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600a01541115156115f4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f332e2052656d61696e20616d6f756e742069732030000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020019081526020016000206002015442111515156116a8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f342e20476966742068617320657870697279000000000000000000000000000081525060200191505060405180910390fd5b600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600a015490506000811115156116f557fe5b600060026000866fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415151561178f57fe5b600083111515611807576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f352e20616d6f756e74206d75737420626967676572207468616e20300000000081525060200191505060405180910390fd5b8183111515156118a5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001807f362e20616d6f756e74206d757374206c657373207468616e2070726552656d6181526020017f696e416d6f756e7400000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8373ffffffffffffffffffffffffffffffffffffffff166108fc849081150290604051600060405180830381858888f193505050501580156118eb573d6000803e3d6000fd5b50600061193560026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600801546001612504565b90508060026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020019081526020016000206008018190555060008114156119d757600160026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060030160006101000a81548160ff0219169083151502179055505b8360026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600b0160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550611a5f8385612504565b60026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600a01819055508473ffffffffffffffffffffffffffffffffffffffff1660026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16876fffffffffffffffffffffffffffffffff19167f072281ae6fcf9661b78f76f7fa1db016572dc7575077128b6b795c343fabacdc876040518082815260200191505060405180910390a4505050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611bd757600080fd5b60008110151515611be457fe5b8060048190555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611c4957600080fd5b60008110151515611c5657fe5b8060058190555050565b600160205281600052604060002081815481101515611c7b57fe5b9060005260206000209060029182820401919006601002915091509054906101000a90047001000000000000000000000000000000000281565b6060600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805480602002602001604051908101604052809291908181526020018280548015611d8657602002820191906000526020600020906000905b82829054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff191681526020019060100190602082600f01049283019260010382029150808411611d2f5790505b50505050509050919050565b60008070010000000000000000000000000000000002836fffffffffffffffffffffffffffffffff191610151515611e32576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642067696674496400000000000000000000000000000000000081525060200191505060405180910390fd5b600060026000856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020600b0160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905060008110151515611ec057600080fd5b8091505092915050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611f2557600080fd5b611f2e8161251d565b50565b600080339050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614151515611f7057fe5b6000349050600060065410151515611f8457fe5b60065481111515612023576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001807f372e2050726f7669646520616d6f756e742063616e206e6f74206c657373207481526020017f68616e207472616e73616374696f6e206665650000000000000000000000000081525060400191505060405180910390fd5b600061203142600354612617565b90506000151560026000876fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060000160009054906101000a900460ff1615151415156120f8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f392e20676966744944206973206578697374730000000000000000000000000081525060200191505060405180910390fd5b60008711151561210457fe5b600061211283600654612504565b90506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6006549081150290604051600060405180830381858888f1935050505015801561217d573d6000803e3d6000fd5b5060008111151561221c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001807f382e205265616c20416d6f756e74206d75737420626967676572207468616e2081526020017f300000000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b61018060405190810160405280600115158152602001876fffffffffffffffffffffffffffffffff191681526020018573ffffffffffffffffffffffffffffffffffffffff1681526020018381526020016000151581526020018a815260200142815260200188600181111561228e57fe5b81526020018981526020018981526020018281526020018281525060026000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060008201518160000160006101000a81548160ff02191690831515021790555060208201518160000160016101000a8154816fffffffffffffffffffffffffffffffff02191690837001000000000000000000000000000000009004021790555060408201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060820151816002015560808201518160030160006101000a81548160ff02191690831515021790555060a08201518160040190805190602001906123d09291906126cd565b5060c0820151816005015560e08201518160060160006101000a81548160ff021916908360018111156123ff57fe5b021790555061010082015181600701556101208201518160080155610140820151816009015561016082015181600a01559050508373ffffffffffffffffffffffffffffffffffffffff16866fffffffffffffffffffffffffffffffff19167f8abca507d9ed393c118d9fd59f06edaf97fc3f9b08df427ca1bb3f3f3a6fb06b83858c8c604051808581526020018481526020018381526020018260018111156124a557fe5b60ff16815260200194505050505060405180910390a385945050505050949350505050565b600060011515826000015115151480156124ec57506000151582608001511515145b80156124fd57506000826101200151115b9050919050565b600082821115151561251257fe5b818303905092915050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415151561255957600080fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000818301905082811015151561262a57fe5b80905092915050565b6101806040519081016040528060001515815260200160006fffffffffffffffffffffffffffffffff19168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581526020016060815260200160008152602001600060018111156126ab57fe5b8152602001600081526020016000815260200160008152602001600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061270e57805160ff191683800117855561273c565b8280016001018555821561273c579182015b8281111561273b578251825591602001919060010190612720565b5b509050612749919061274d565b5090565b61276f91905b8082111561276b576000816000905550600101612753565b5090565b9056fea165627a7a723058201896d1e809125a6b288da360602a40800a22789936da2d9289ea32cfbcd8067b0029";

    public static final String FUNC_DESTROYCONTRACT = "destroyContract";

    public static final String FUNC_SETTRANSACTIONFEE = "setTransactionFee";

    public static final String FUNC_GIFTIDTOGIFT = "giftIdToGift";

    public static final String FUNC_SETEXPIRYTIME = "setExpiryTime";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REDEEM = "redeem";

    public static final String FUNC_SETMINRATE = "setMinRate";

    public static final String FUNC_SETMAXRATE = "setMaxRate";

    public static final String FUNC_RECIPIENTTOGIFTIDS = "recipientToGiftIds";

    public static final String FUNC_GETGIFTIDSBYRECIPIENT = "getGiftIdsByRecipient";

    public static final String FUNC_GETREDEEMEDAMOUNT = "getRedeemedAmount";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_GIVE = "give";

    public static final Event CONSTRUCTED_EVENT = new Event("Constructed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}), new ArrayList<>());
    ;

    public static final Event COLLECTEDALLFEES_EVENT = new Event("CollectedAllFees",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}), new ArrayList<>());
    ;

    public static final Event DIRECTLYDEPOSITED_EVENT = new Event("DirectlyDeposited",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}), new ArrayList<>());
    ;

    public static final Event GAVE_EVENT = new Event("Gave",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Address>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event REDEEMED_EVENT = new Event("Redeemed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPRENOUNCED_EVENT = new Event("OwnershipRenounced",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}), new ArrayList<>());
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}), new ArrayList<>());
    ;

    protected NewRedPackage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NewRedPackage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> destroyContract(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DESTROYCONTRACT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> setTransactionFee(Uint256 newTransactionFee, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SETTRANSACTIONFEE,
                Arrays.<Type>asList(newTransactionFee),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple12<Bool, Bytes16, Address, Uint256, Bool, Utf8String, Uint256, Uint8, Uint256, Uint256, Uint256, Uint256>> giftIdToGift(Bytes16 param0) {
        final Function function = new Function(FUNC_GIFTIDTOGIFT,
                Arrays.<Type>asList(param0),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Bytes16>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple12<Bool, Bytes16, Address, Uint256, Bool, Utf8String, Uint256, Uint8, Uint256, Uint256, Uint256, Uint256>>(
                new Callable<Tuple12<Bool, Bytes16, Address, Uint256, Bool, Utf8String, Uint256, Uint8, Uint256, Uint256, Uint256, Uint256>>() {
                    @Override
                    public Tuple12<Bool, Bytes16, Address, Uint256, Bool, Utf8String, Uint256, Uint8, Uint256, Uint256, Uint256, Uint256> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple12<Bool, Bytes16, Address, Uint256, Bool, Utf8String, Uint256, Uint8, Uint256, Uint256, Uint256, Uint256>(
                                (Bool) results.get(0),
                                (Bytes16) results.get(1),
                                (Address) results.get(2),
                                (Uint256) results.get(3),
                                (Bool) results.get(4),
                                (Utf8String) results.get(5),
                                (Uint256) results.get(6),
                                (Uint8) results.get(7),
                                (Uint256) results.get(8),
                                (Uint256) results.get(9),
                                (Uint256) results.get(10),
                                (Uint256) results.get(11));
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setExpiryTime(Uint256 newExpiryTime, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SETEXPIRYTIME,
                Arrays.<Type>asList(newExpiryTime),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> withdraw(Bytes16 giftId, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_WITHDRAW,
                Arrays.<Type>asList(giftId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> redeem(Bytes16 giftId, Address receiptAddress, Uint256 amount) {
        final Function function = new Function(
                FUNC_REDEEM,
                Arrays.<Type>asList(giftId, receiptAddress, amount),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setMinRate(Uint256 newMinRate, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SETMINRATE,
                Arrays.<Type>asList(newMinRate),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> setMaxRate(Uint256 newMaxRate, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SETMAXRATE,
                Arrays.<Type>asList(newMaxRate),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Bytes16> recipientToGiftIds(Address param0, Uint256 param1) {
        final Function function = new Function(FUNC_RECIPIENTTOGIFTIDS,
                Arrays.<Type>asList(param0, param1),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<DynamicArray<Bytes16>> getGiftIdsByRecipient(Address recipient) {
        final Function function = new Function(FUNC_GETGIFTIDSBYRECIPIENT,
                Arrays.<Type>asList(recipient),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes16>>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint256> getRedeemedAmount(Bytes16 giftId, Address receiptAddress) {
        final Function function = new Function(FUNC_GETREDEEMEDAMOUNT,
                Arrays.<Type>asList(giftId, receiptAddress),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(Address _newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(_newOwner),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> give(Utf8String message, Uint256 totalPackageCount, Uint8 luckType, Bytes16 uuid, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_GIVE,
                Arrays.<Type>asList(message, totalPackageCount, luckType, uuid),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public static RemoteCall<NewRedPackage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(NewRedPackage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static RemoteCall<NewRedPackage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(NewRedPackage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public List<ConstructedEventResponse> getConstructedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONSTRUCTED_EVENT, transactionReceipt);
        ArrayList<ConstructedEventResponse> responses = new ArrayList<ConstructedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ConstructedEventResponse typedResponse = new ConstructedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.by = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ConstructedEventResponse> constructedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ConstructedEventResponse>() {
            @Override
            public ConstructedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONSTRUCTED_EVENT, log);
                ConstructedEventResponse typedResponse = new ConstructedEventResponse();
                typedResponse.log = log;
                typedResponse.by = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<ConstructedEventResponse> constructedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONSTRUCTED_EVENT));
        return constructedEventObservable(filter);
    }

    public List<CollectedAllFeesEventResponse> getCollectedAllFeesEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COLLECTEDALLFEES_EVENT, transactionReceipt);
        ArrayList<CollectedAllFeesEventResponse> responses = new ArrayList<CollectedAllFeesEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CollectedAllFeesEventResponse typedResponse = new CollectedAllFeesEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.by = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CollectedAllFeesEventResponse> collectedAllFeesEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, CollectedAllFeesEventResponse>() {
            @Override
            public CollectedAllFeesEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(COLLECTEDALLFEES_EVENT, log);
                CollectedAllFeesEventResponse typedResponse = new CollectedAllFeesEventResponse();
                typedResponse.log = log;
                typedResponse.by = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<CollectedAllFeesEventResponse> collectedAllFeesEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COLLECTEDALLFEES_EVENT));
        return collectedAllFeesEventObservable(filter);
    }

    public List<DirectlyDepositedEventResponse> getDirectlyDepositedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DIRECTLYDEPOSITED_EVENT, transactionReceipt);
        ArrayList<DirectlyDepositedEventResponse> responses = new ArrayList<DirectlyDepositedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DirectlyDepositedEventResponse typedResponse = new DirectlyDepositedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DirectlyDepositedEventResponse> directlyDepositedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, DirectlyDepositedEventResponse>() {
            @Override
            public DirectlyDepositedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DIRECTLYDEPOSITED_EVENT, log);
                DirectlyDepositedEventResponse typedResponse = new DirectlyDepositedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<DirectlyDepositedEventResponse> directlyDepositedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIRECTLYDEPOSITED_EVENT));
        return directlyDepositedEventObservable(filter);
    }

    public List<GaveEventResponse> getGaveEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(GAVE_EVENT, transactionReceipt);
        ArrayList<GaveEventResponse> responses = new ArrayList<GaveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GaveEventResponse typedResponse = new GaveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.giftId = (Bytes16) eventValues.getIndexedValues().get(0);
            typedResponse.giver = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.expiry = (Uint256) eventValues.getNonIndexedValues().get(1);
            typedResponse.redPackageCount = (Uint256) eventValues.getNonIndexedValues().get(2);
            typedResponse.luckType = (Uint8) eventValues.getNonIndexedValues().get(3);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<GaveEventResponse> gaveEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, GaveEventResponse>() {
            @Override
            public GaveEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(GAVE_EVENT, log);
                GaveEventResponse typedResponse = new GaveEventResponse();
                typedResponse.log = log;
                typedResponse.giftId = (Bytes16) eventValues.getIndexedValues().get(0);
                typedResponse.giver = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse.expiry = (Uint256) eventValues.getNonIndexedValues().get(1);
                typedResponse.redPackageCount = (Uint256) eventValues.getNonIndexedValues().get(2);
                typedResponse.luckType = (Uint8) eventValues.getNonIndexedValues().get(3);
                return typedResponse;
            }
        });
    }

    public Observable<GaveEventResponse> gaveEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GAVE_EVENT));
        return gaveEventObservable(filter);
    }

    public List<RedeemedEventResponse> getRedeemedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REDEEMED_EVENT, transactionReceipt);
        ArrayList<RedeemedEventResponse> responses = new ArrayList<RedeemedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RedeemedEventResponse typedResponse = new RedeemedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.giftId = (Bytes16) eventValues.getIndexedValues().get(0);
            typedResponse.giver = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.recipient = (Address) eventValues.getIndexedValues().get(2);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RedeemedEventResponse> redeemedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, RedeemedEventResponse>() {
            @Override
            public RedeemedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEMED_EVENT, log);
                RedeemedEventResponse typedResponse = new RedeemedEventResponse();
                typedResponse.log = log;
                typedResponse.giftId = (Bytes16) eventValues.getIndexedValues().get(0);
                typedResponse.giver = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.recipient = (Address) eventValues.getIndexedValues().get(2);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<RedeemedEventResponse> redeemedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEMED_EVENT));
        return redeemedEventObservable(filter);
    }

    public List<OwnershipRenouncedEventResponse> getOwnershipRenouncedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPRENOUNCED_EVENT, transactionReceipt);
        ArrayList<OwnershipRenouncedEventResponse> responses = new ArrayList<OwnershipRenouncedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipRenouncedEventResponse typedResponse = new OwnershipRenouncedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipRenouncedEventResponse> ownershipRenouncedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipRenouncedEventResponse>() {
            @Override
            public OwnershipRenouncedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPRENOUNCED_EVENT, log);
                OwnershipRenouncedEventResponse typedResponse = new OwnershipRenouncedEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<OwnershipRenouncedEventResponse> ownershipRenouncedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPRENOUNCED_EVENT));
        return ownershipRenouncedEventObservable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventObservable(filter);
    }

    public static NewRedPackage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NewRedPackage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NewRedPackage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NewRedPackage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ConstructedEventResponse {
        public Log log;

        public Address by;

        public Uint256 amount;
    }

    public static class CollectedAllFeesEventResponse {
        public Log log;

        public Address by;

        public Uint256 amount;
    }

    public static class DirectlyDepositedEventResponse {
        public Log log;

        public Address from;

        public Uint256 amount;
    }

    public static class GaveEventResponse {
        public Log log;

        public Bytes16 giftId;

        public Address giver;

        public Uint256 amount;

        public Uint256 expiry;

        public Uint256 redPackageCount;

        public Uint8 luckType;
    }

    public static class RedeemedEventResponse {
        public Log log;

        public Bytes16 giftId;

        public Address giver;

        public Address recipient;

        public Uint256 amount;
    }

    public static class OwnershipRenouncedEventResponse {
        public Log log;

        public Address previousOwner;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public Address previousOwner;

        public Address newOwner;
    }
}

