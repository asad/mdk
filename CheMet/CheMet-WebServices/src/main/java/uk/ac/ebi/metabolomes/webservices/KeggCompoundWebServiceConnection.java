package uk.ac.ebi.metabolomes.webservices;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;

import java.util.HashMap;
import javax.xml.rpc.ServiceException;

import keggapi.KEGGLocator;
import keggapi.KEGGPortType;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;

public class KeggCompoundWebServiceConnection extends ChemicalDBWebService {

    private KEGGPortType serv;
    private final String serviceProviderName = "BioCyc";
    private Logger logger = Logger.getLogger(KeggCompoundWebServiceConnection.class.getName());
    private final int bgetMaxQueries = 100;

    private enum KeggDBs {
        COMPOUND ("cpd"), DRUG ("dr"), GLYCAN ("gl");

        private final String prefix;
        KeggDBs(String prefix) {
            this.prefix = prefix;
        }

        String getDBPrefixForBget() {
            return this.prefix;
        }
    }
    
    public static void main(String args[]) {
    	//KeggCompoundWebServiceConnection keggConn = new KeggCompoundWebServiceConnection();
        ChemicalDBWebService keggConn = new KeggCompoundWebServiceConnection();
        if(args != null && args.length>0) {
            ArrayList<IAtomContainer> mols = keggConn.downloadMolsToCDKObject(args);
            for(IAtomContainer mol : mols) {
        		System.out.println("ID: "+mol.getID());
            }
        }
        String[] compIds = { "C07688", "C07689", "D00608", "D00609" };
        try {
            String res = ((KeggCompoundWebServiceConnection)keggConn).executeBget(compIds);
            System.out.println(res);
            HashMap<String, String> testres = ((KeggCompoundWebServiceConnection)keggConn).resolveNewIDsForObsoleteEntrys(res);
            for(String key : testres.keySet()) {
                System.out.println(key+"\t"+testres.get(key));
                ArrayList<String> name = new ArrayList<String>();
                name.add(testres.get(key));
                ArrayList<IAtomContainer> mols = keggConn.downloadMolsToCDKObject(name);
                if(mols.size() > 0 && mols.get(0) != null )
                    System.out.println("We have struct for: "+mols.get(0).getID());
            }

        } catch (RemoteException ex) {

        }
    }

    public KeggCompoundWebServiceConnection() {
		this.init();
    }


    public HashMap<String, String> mapAnyObsoleteID2NewID(ArrayList<String> obsIDs) {
        String[] cpds = obsIDs.toArray(new String[obsIDs.size()]);
        try {
            return this.resolveNewIDsForObsoleteEntrys(this.executeBget(cpds));
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds ids through bget: ", ex);
            return null;
        }
    }

    private String executeBget(String[] cpds) throws RemoteException {
        String totalAns = "";
        logger.debug("Amount of cpds: "+cpds.length);
        for (int i = 0; i < cpds.length; i += this.bgetMaxQueries) {
            String bgetQuery = "";
            String[] subCPDS = new String[Math.min(this.bgetMaxQueries, cpds.length - i)];
            int lengthOfThisIteration = Math.min(this.bgetMaxQueries, cpds.length - i);
            // j is the pointer for cpds, j-i is the pointer for subCPDS
            for (int j = i; j < i + subCPDS.length; j++) {
                subCPDS[j - i] = cpds[j];
            }
            for (String cpd : subCPDS) {
                bgetQuery += this.resolveDBPrefix(cpd) + ":" + cpd + " ";
            }
            logger.debug("Answer before bget: "+totalAns.length());
            totalAns += serv.bget(bgetQuery);
            logger.debug("Answer after bget: "+totalAns.length());
        }
        return totalAns;
    }

    public String[] findCompoundByName(String name) {
        try {
            return serv.search_compounds_by_name(name);
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds by name", ex);
            return null;
        }
    }

    private String resolveDBPrefix(String id) {
        if(id.toLowerCase().startsWith("c"))
            return KeggDBs.COMPOUND.getDBPrefixForBget();
        if(id.toLowerCase().startsWith("d"))
            return KeggDBs.DRUG.getDBPrefixForBget();
        if(id.toLowerCase().startsWith("g"))
            return KeggDBs.GLYCAN.getDBPrefixForBget();
        return null;
    }


    /*
     * This function should be able to handle this output from kegg
     *
     * ENTRY       C07689            Obsolete  Compound
     * NAME        Transferred to D00609
     * ///
     *
     * */
    private HashMap<String, String> resolveNewIDsForObsoleteEntrys(String keggBgetResponse) {
        String[] lines = keggBgetResponse.split("\n");
        String obsId = null;
        String newId = null;
        HashMap<String, String> res = new HashMap<String, String>();
        for(String line : lines) {
            if(line.startsWith("ENTRY") && line.contains("Obsolete")) {
                String tokens[] = line.split("\\s+");
                if(tokens.length > 2)
                    obsId = tokens[1];
            } else if(line.startsWith("NAME") && line.contains("Transferred to")) {
                String tokens[] = line.split("\\s+");
                if(tokens.length > 2)
                    newId = tokens[tokens.length-1];
            } else if(line.startsWith("///")) {
                if(obsId != null && newId != null)
                    res.put(obsId, newId);
                obsId=null;
                newId=null;
            }
        }
        return res;
    }
	
	private void init() {
		KEGGLocator locator = new KEGGLocator();
		try {
			serv = locator.getKEGGPort();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
		// TODO Auto-generated method stub
		ArrayList<IAtomContainer> mols = new ArrayList<IAtomContainer>();
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		for(String id : ids) {
			try {
				String molTxt = this.downloadMolToString(id);
				MDLV2000Reader reader = new MDLV2000Reader(new StringReader(molTxt));
				IAtomContainer mol = (IAtomContainer) reader.read(builder.newInstance(IMolecule.class));
				if(mol == null) {
					logger.warn("Null CDK object for Kegg ID:"+id+", number of ids: "+ids.length);
					continue;
				}
				mol.setID(id);
				mols.add(mol);
			} catch (CDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                                logger.error("Could not generate CDK molecule for id "+id, e);
			}
		}
		return mols;
	}

        public String downloadMolToString(String id) {
            String molTxt=null;
            try {
                molTxt = serv.bget("-f m "+this.resolveDBPrefix(id)+":"+id);
            } catch (RemoteException e) {
                logger.error("Could not get id "+id+" from KeggWebService", e);
            }
            return molTxt;
        }

        public String[] downloadCompoundIDsForECNumber(String ecnumber) {
            String[] cpdIds = null;
            try {
                cpdIds = serv.get_compounds_by_enzyme("ec:" + ecnumber);
            } catch (RemoteException ex) {
                logger.error("Could not retrieve compounds for ec number "+ecnumber, ex);
            }
            return cpdIds;
        }

        public String[] downloadCompoundIDsForReaction(String rxnID) {
            String[] cpdIds = null;
            try {
                cpdIds = serv.get_compounds_by_reaction("rn:"+rxnID);
            } catch (RemoteException ex) {
                logger.error("Could not retrieve compounds for reaction "+rxnID, ex);
            }
            return cpdIds;
        }

        public Boolean[] areCompoundsGeneric(String[] cpds) {
            if(cpds == null)
                return null;
            Boolean[] ans = new Boolean[cpds.length];
        try {
            String bgetAns = executeBget(cpds);
        } catch (RemoteException ex) {
            logger.error( "Error in bget query", ex);
        }
            return null;
        }

	@Override
	public String getServiceProviderName() {
		// TODO Auto-generated method stub
		return this.serviceProviderName;
	}

    @Override
    public HashMap<String, String> searchByInChI(String inchi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}