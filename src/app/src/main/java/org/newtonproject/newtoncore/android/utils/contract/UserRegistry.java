package org.newtonproject.newtoncore.android.utils.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
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
public class UserRegistry extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550613583806100616000396000f3fe608060405260043610610072576000357c01000000000000000000000000000000000000000000000000000000009004806365fff43d14610077578063a87430ba146100b2578063af68b3d614610143578063d70fcb6f146101be578063e8a4c04e1461020f578063f647f82b14610317575b600080fd5b34801561008357600080fd5b506100b06004803603602081101561009a57600080fd5b81019080803590602001909291905050506103dc565b005b3480156100be57600080fd5b50610101600480360360208110156100d557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506104ab565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561014f57600080fd5b5061017c6004803603602081101561016657600080fd5b81019080803590602001909291905050506104de565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101ca57600080fd5b5061020d600480360360208110156101e157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610511565b005b34801561021b57600080fd5b506102d56004803603602081101561023257600080fd5b810190808035906020019064010000000081111561024f57600080fd5b82018360208201111561026157600080fd5b8035906020019184600183028401116401000000008311171561028357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061061a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103da6004803603604081101561032d57600080fd5b81019080803590602001909291908035906020019064010000000081111561035457600080fd5b82018360208201111561036657600080fd5b8035906020019184600183028401116401000000008311171561038857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610639565b005b3373ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156104a1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f7065726d697373696f6e2064656e69656400000000000000000000000000000081525060200191505060405180910390fd5b8060048190555050565b60006020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b3373ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156105d6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f7065726d697373696f6e2064656e69656400000000000000000000000000000081525060200191505060405180910390fd5b80600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000808280519060200120905080600052600051915081915050919050565b60045434101515156106b3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f76616c7565206973206c6f776572207468616e2066656500000000000000000081525060200191505060405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff166106d38261061a565b73ffffffffffffffffffffffffffffffffffffffff1614151561075e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f7075626c6963206b6579206973206e6f7420666f72206164647265737300000081525060200191505060405180910390fd5b818180519060200120141515610802576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f7075626c6963206b65792063616e27742067656e6572617465206964656e746981526020017f747900000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff166000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610904576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f6164647265737320697320616c7265616479207265676973746572656400000081525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff166001600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561097257600080fd5b6000338261097e610c56565b808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156109ed5780820151818401526020810190506109d2565b50505050905090810190601f168015610a1a5780820380516001836020036101000a031916815260200191505b509350505050604051809103906000f080158015610a3c573d6000803e3d6000fd5b509050806000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806001600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6004549081150290604051600060405180830381858888f19350505050158015610b78573d6000803e3d6000fd5b507fd9c61834873d152de0b14b374ad77c59783d9d12aac0b5ed73a55fd82973bde1338484604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610c15578082015181840152602081019050610bfa565b50505050905090810190601f168015610c425780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a1505050565b6040516128f180610c678339019056fe60806040523480156200001157600080fd5b50604051620028f1380380620028f1833981018060405260408110156200003757600080fd5b810190808051906020019092919080516401000000008111156200005a57600080fd5b828101905060208101848111156200007157600080fd5b81518560018202830111640100000000821117156200008f57600080fd5b5050929190505050600082604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c0100000000000000000000000002815260140191505060405160208183030381529060405280519060200120905080600160008381526020019081526020016000206002018190555060018060008381526020019081526020016000206000018190555060018060008381526020019081526020016000206001018190555060026000600181526020019081526020016000208190806001815401808255809150509060018203906000526020600020016000909192909190915055508160069080519060200190620001a4929190620001f3565b50600180600083815260200190815260200160002060000154827f480000bb1edad8ca1470381cc334b1917fbd51c6531f3a623ea8e0ec7e38a6e960405160405180910390a4505050620002a2565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200023657805160ff191683800117855562000267565b8280016001018555821562000267579182015b828111156200026657825182559160200191906001019062000249565b5b5090506200027691906200027a565b5090565b6200029f91905b808211156200029b57600081600090555060010162000281565b5090565b90565b61263f80620002b26000396000f3fe6080604052600436106100bf576000357c010000000000000000000000000000000000000000000000000000000090048063032c1a8a146100c457806312aaac70146101135780631d38124014610170578063262b54f5146101d75780632e334452146102675780634eee424a146102f7578063747442d31461034a578063862642f5146103a95780639010f726146103fc578063b1a34e0d1461048c578063b61d27f6146106ca578063c9100bcb146107c3578063d202158d14610990575b600080fd5b3480156100d057600080fd5b506100fd600480360360208110156100e757600080fd5b81019080803590602001909291905050506109ed565b6040518082815260200191505060405180910390f35b34801561011f57600080fd5b5061014c6004803603602081101561013657600080fd5b8101908080359060200190929190505050610a0d565b60405180848152602001838152602001828152602001935050505060405180910390f35b34801561017c57600080fd5b506101bd6004803603606081101561019357600080fd5b81019080803590602001909291908035906020019092919080359060200190929190505050610a64565b604051808215151515815260200191505060405180910390f35b3480156101e357600080fd5b50610210600480360360208110156101fa57600080fd5b8101908080359060200190929190505050610cf8565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610253578082015181840152602081019050610238565b505050509050019250505060405180910390f35b34801561027357600080fd5b5061027c610d63565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102bc5780820151818401526020810190506102a1565b50505050905090810190601f1680156102e95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561030357600080fd5b506103306004803603602081101561031a57600080fd5b8101908080359060200190929190505050610e05565b604051808215151515815260200191505060405180910390f35b34801561035657600080fd5b5061038f6004803603604081101561036d57600080fd5b8101908080359060200190929190803515159060200190929190505050611258565b604051808215151515815260200191505060405180910390f35b3480156103b557600080fd5b506103e2600480360360208110156103cc57600080fd5b81019080803590602001909291905050506117c3565b604051808215151515815260200191505060405180910390f35b34801561040857600080fd5b506104356004803603602081101561041f57600080fd5b81019080803590602001909291905050506118fa565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561047857808201518184015260208101905061045d565b505050509050019250505060405180910390f35b34801561049857600080fd5b506106b4600480360360c08110156104af57600080fd5b810190808035906020019092919080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561050057600080fd5b82018360208201111561051257600080fd5b8035906020019184600183028401116401000000008311171561053457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561059757600080fd5b8201836020820111156105a957600080fd5b803590602001918460018302840111640100000000831117156105cb57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561062e57600080fd5b82018360208201111561064057600080fd5b8035906020019184600183028401116401000000008311171561066257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611965565b6040518082815260200191505060405180910390f35b6107ad600480360360608110156106e057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561072757600080fd5b82018360208201111561073957600080fd5b8035906020019184600183028401116401000000008311171561075b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611e6a565b6040518082815260200191505060405180910390f35b3480156107cf57600080fd5b506107fc600480360360208110156107e657600080fd5b810190808035906020019092919050505061216e565b604051808781526020018681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200180602001848103845287818151815260200191508051906020019080838360005b83811015610882578082015181840152602081019050610867565b50505050905090810190601f1680156108af5780820380516001836020036101000a031916815260200191505b50848103835286818151815260200191508051906020019080838360005b838110156108e85780820151818401526020810190506108cd565b50505050905090810190601f1680156109155780820380516001836020036101000a031916815260200191505b50848103825285818151815260200191508051906020019080838360005b8381101561094e578082015181840152602081019050610933565b50505050905090810190601f16801561097b5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b34801561099c57600080fd5b506109d3600480360360408110156109b357600080fd5b810190808035906020019092919080359060200190929190505050612408565b604051808215151515815260200191505060405180910390f35b600060016000838152602001908152602001600020600001549050919050565b60008060006001600085815260200190815260200160002060000154600160008681526020019081526020016000206001015460016000878152602001908152602001600020600201549250925092509193909250565b600083600160008681526020019081526020016000206002015414151515610af4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f4b657920616c726561647920657869737473000000000000000000000000000081525060200191505060405180910390fd5b3073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610c3057610b9533604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206001612408565b1515610c2f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f53656e64657220646f6573206e6f742068617665206d616e6167656d656e742081526020017f6b6579000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b5b836001600086815260200190815260200160002060020181905550826001600086815260200190815260200160002060000181905550816001600086815260200190815260200160002060010181905550600260008481526020019081526020016000208490806001815401808255809150509060018203906000526020600020016000909192909190915055508183857f480000bb1edad8ca1470381cc334b1917fbd51c6531f3a623ea8e0ec7e38a6e960405160405180910390a4600190509392505050565b606060056000838152602001908152602001600020805480602002602001604051908101604052809291908181526020018280548015610d5757602002820191906000526020600020905b815481526020019060010190808311610d43575b50505050509050919050565b606060068054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610dfb5780601f10610dd057610100808354040283529160200191610dfb565b820191906000526020600020905b815481529060010190602001808311610dde57829003601f168201915b5050505050905090565b60003073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610f4357610ea833604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206001612408565b1515610f42576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f53656e64657220646f6573206e6f742068617665206d616e6167656d656e742081526020017f6b6579000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b5b6004600083815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166004600084815260200190815260200160002060000154837f3cf57863a89432c61c4a27073c6ee39e8a764bff5a05aebfbcdcdc80b2e6130a60046000878152602001908152602001600020600101546004600088815260200190815260200160002060030160046000898152602001908152602001600020600401600460008a8152602001908152602001600020600501604051808581526020018060200180602001806020018481038452878181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156110b95780601f1061108e576101008083540402835291602001916110b9565b820191906000526020600020905b81548152906001019060200180831161109c57829003601f168201915b505084810383528681815460018160011615610100020316600290048152602001915080546001816001161561010002031660029004801561113c5780601f106111115761010080835404028352916020019161113c565b820191906000526020600020905b81548152906001019060200180831161111f57829003601f168201915b50508481038252858181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156111bf5780601f10611194576101008083540402835291602001916111bf565b820191906000526020600020905b8154815290600101906020018083116111a257829003601f168201915b505097505050505050505060405180910390a46004600083815260200190815260200160002060008082016000905560018201600090556002820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905560038201600061122d919061245e565b60048201600061123d919061245e565b60058201600061124d91906124a6565b505060019050919050565b60006112c633604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206002612408565b151561133a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f53656e64657220646f6573206e6f74206861766520616374696f6e206b65790081525060200191505060405180910390fd5b6060837fb3932da477fe5d6c8ff2eafef050c0f3a1af18fc07121001482600f36f3715d884604051808215151515815260200191505060405180910390a26001151583151514156117885760016003600086815260200190815260200160002060030160006101000a81548160ff0219169083151502179055506003600085815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660036000868152602001908152602001600020600101546003600087815260200190815260200160002060020160405180828054600181600116156101000203166002900480156114895780601f10611467576101008083540402835291820191611489565b820191906000526020600020905b815481529060010190602001808311611475575b505091505060006040518083038185875af1925050503d80600081146114cb576040519150601f19603f3d011682016040523d82523d6000602084013e6114d0565b606091505b508092508193505050811561164b5760016003600086815260200190815260200160002060030160016101000a81548160ff02191690831515021790555060036000858152602001908152602001600020600101546003600086815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16857f1f920dbda597d7bf95035464170fa58d0a4b57f13a1c315ace6793b9f63688b86003600089815260200190815260200160002060020160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156116335780601f1061160857610100808354040283529160200191611633565b820191906000526020600020905b81548152906001019060200180831161161657829003601f168201915b50509250505060405180910390a460019150506117bd565b60036000858152602001908152602001600020600101546003600086815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16857fe10c49d9f7c71da23262367013434763cfdb2332267641728d25cd712c5c6a686003600089815260200190815260200160002060020160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156117705780601f1061174557610100808354040283529160200191611770565b820191906000526020600020905b81548152906001019060200180831161175357829003601f168201915b50509250505060405180910390a460009150506117bd565b60006003600086815260200190815260200160002060030160006101000a81548160ff02191690831515021790555060019150505b92915050565b6000816001600084815260200190815260200160002060020154141515611852576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f2073756368206b657900000000000000000000000000000000000000000081525060200191505060405180910390fd5b6001600083815260200190815260200160002060010154600160008481526020019081526020016000206000015460016000858152602001908152602001600020600201547f585a4aef50f8267a92b32412b331b20f7f8b96f2245b253b9cc50dcc621d339760405160405180910390a46001600083815260200190815260200160002060008082016000905560018201600090556002820160009055505060019050919050565b60606002600083815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561195957602002820191906000526020600020905b815481526020019060010190808311611945575b50505050509050919050565b6000808588604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401828152602001925050506040516020818303038152906040528051906020012090503073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611b1057611a7533604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206003612408565b1515611b0f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001807f53656e64657220646f6573206e6f74206861766520636c61696d207369676e6581526020017f72206b657900000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b5b8573ffffffffffffffffffffffffffffffffffffffff166004600083815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515611bb957600560008981526020019081526020016000208190806001815401808255809150509060018203906000526020600020016000909192909190915055505b876004600083815260200190815260200160002060000181905550866004600083815260200190815260200160002060010181905550856004600083815260200190815260200160002060020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555084600460008381526020019081526020016000206003019080519060200190611c6e9291906124ee565b5083600460008381526020019081526020016000206004019080519060200190611c999291906124ee565b5082600460008381526020019081526020016000206005019080519060200190611cc492919061256e565b508573ffffffffffffffffffffffffffffffffffffffff1688827f46149b18aa084502c3f12bc75e19eda8bda8d102b82cce8474677a6d0d5f43c58a89898960405180858152602001806020018060200180602001848103845287818151815260200191508051906020019080838360005b83811015611d51578082015181840152602081019050611d36565b50505050905090810190601f168015611d7e5780820380516001836020036101000a031916815260200191505b50848103835286818151815260200191508051906020019080838360005b83811015611db7578082015181840152602081019050611d9c565b50505050905090810190601f168015611de45780820380516001836020036101000a031916815260200191505b50848103825285818151815260200191508051906020019080838360005b83811015611e1d578082015181840152602081019050611e02565b50505050905090810190601f168015611e4a5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a4809150509695505050505050565b6000600360008054815260200190815260200160002060030160019054906101000a900460ff16151515611f06576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f416c72656164792065786563757465640000000000000000000000000000000081525060200191505060405180910390fd5b83600360008054815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550826003600080548152602001908152602001600020600101819055508160036000805481526020019081526020016000206002019080519060200190611fa39291906124ee565b50828473ffffffffffffffffffffffffffffffffffffffff166000547f8afcfabcb00e47a53a8fc3e9f23ff47ee1926194bb1350dd007c50b412a6cee8856040518080602001828103825283818151815260200191508051906020019080838360005b83811015612021578082015181840152602081019050612006565b50505050905090810190601f16801561204e5780820380516001836020036101000a031916815260200191505b509250505060405180910390a46120c733604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206001612408565b8061213a575061213933604051602001808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401915050604051602081830303815290604052805190602001206002612408565b5b1561214e5761214c6000546001611258565b505b600080815480929190600101919050555060016000540390509392505050565b6000806000606080606060046000888152602001908152602001600020600001546004600089815260200190815260200160002060010154600460008a815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600460008b8152602001908152602001600020600301600460008c8152602001908152602001600020600401600460008d8152602001908152602001600020600501828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156122b45780601f10612289576101008083540402835291602001916122b4565b820191906000526020600020905b81548152906001019060200180831161229757829003601f168201915b50505050509250818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156123505780601f1061232557610100808354040283529160200191612350565b820191906000526020600020905b81548152906001019060200180831161233357829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156123ec5780601f106123c1576101008083540402835291602001916123ec565b820191906000526020600020905b8154815290600101906020018083116123cf57829003601f168201915b5050505050905095509550955095509550955091939550919395565b600080600060010260016000868152602001908152602001600020600201541415612437576000915050612458565b82600160008681526020019081526020016000206000015411159050809150505b92915050565b50805460018160011615610100020316600290046000825580601f1061248457506124a3565b601f0160209004906000526020600020908101906124a291906125ee565b5b50565b50805460018160011615610100020316600290046000825580601f106124cc57506124eb565b601f0160209004906000526020600020908101906124ea91906125ee565b5b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061252f57805160ff191683800117855561255d565b8280016001018555821561255d579182015b8281111561255c578251825591602001919060010190612541565b5b50905061256a91906125ee565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106125af57805160ff19168380011785556125dd565b828001600101855582156125dd579182015b828111156125dc5782518255916020019190600101906125c1565b5b5090506125ea91906125ee565b5090565b61261091905b8082111561260c5760008160009055506001016125f4565b5090565b9056fea165627a7a723058201501a013b0f39548b950a61306972cc47e058aee90b46b02c2bc0def9149bbb30029a165627a7a723058203e576237515a26a960ee03dbda9bc9910454d50939923d9ed24cdb1408cf01bf0029";

    public static final String FUNC_SETFEEOFREGISTERUSER = "setFeeOfRegisterUser";

    public static final String FUNC_USERS = "users";

    public static final String FUNC_IDENTITYRELATIONS = "identityRelations";

    public static final String FUNC_SETNEWPOOLADDRESS = "setNewPoolAddress";

    public static final String FUNC_CALCULATEADDRESS = "calculateAddress";

    public static final String FUNC_REGISTERUSER = "registerUser";

    public static final Event NEWUSER_EVENT = new Event("NewUser", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    protected UserRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected UserRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> setFeeOfRegisterUser(BigInteger _feeOfRegisterUser) {
        final Function function = new Function(
                FUNC_SETFEEOFREGISTERUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_feeOfRegisterUser)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> users(String param0) {
        final Function function = new Function(FUNC_USERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> identityRelations(byte[] param0) {
        final Function function = new Function(FUNC_IDENTITYRELATIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setNewPoolAddress(String _newPoolAddress) {
        final Function function = new Function(
                FUNC_SETNEWPOOLADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_newPoolAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> calculateAddress(byte[] pub) {
        final Function function = new Function(FUNC_CALCULATEADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(pub)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> registerUser(byte[] identity, byte[] publicKey, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_REGISTERUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(identity), 
                new org.web3j.abi.datatypes.DynamicBytes(publicKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public static RemoteCall<UserRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(UserRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<UserRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(UserRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public List<NewUserEventResponse> getNewUserEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWUSER_EVENT, transactionReceipt);
        ArrayList<NewUserEventResponse> responses = new ArrayList<NewUserEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewUserEventResponse typedResponse = new NewUserEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._address = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._identity = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._publicKey = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewUserEventResponse> newUserEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewUserEventResponse>() {
            @Override
            public NewUserEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWUSER_EVENT, log);
                NewUserEventResponse typedResponse = new NewUserEventResponse();
                typedResponse.log = log;
                typedResponse._address = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._identity = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._publicKey = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<NewUserEventResponse> newUserEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWUSER_EVENT));
        return newUserEventObservable(filter);
    }

    public static UserRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static UserRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class NewUserEventResponse {
        public Log log;

        public String _address;

        public byte[] _identity;

        public byte[] _publicKey;
    }
}
