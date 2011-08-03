package uk.ac.ebi.metabolomes.webservices;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;

import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.*;

public class ChEBIWebServiceConnection extends ChemicalDBWebService {

    /**
     * @param args
     */
    private ChebiWebServiceClient client;
    private String serviceProviderName = "ChEBI";
    private Logger logger;
    private int maxResultsSearch;

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        ChEBIWebServiceConnection c = new ChEBIWebServiceConnection();
        String ids[] = {"33568", "15377", "143213"};
        for (String num : ids) {
            System.out.println("InCHI : " + c.getInChIs(args).get(num));
            System.out.println("InCHIKey :" + c.getInChIKeys(args).get(num));
        }
        String defaultPath = "/Users/pmoreno/structures/";
        c.downloadStructureFiles(args, defaultPath);

        String[] idsKegg = { "C06561", "C12087", "C14458", "C00509", "C16232", "C09826", "C09751", "C09047", "C01592", "C08578", "C01263", "C17673", "C15567", "C09614", "C03567"};

        for(String kegg : idsKegg) {
            HashMap<String,String> res = c.searchBy(kegg, SearchCategory.DATABASE_LINK_REGISTRY_NUMBER_CITATION);
            for(String key : res.keySet()) {
                System.out.println(kegg+"\t"+key+"\t"+res.get(key));
            }
        }
    }

    private void init() {
        client = new ChebiWebServiceClient();
        maxResultsSearch = 200;
        logger = Logger.getLogger(ChEBIWebServiceConnection.class.getName());
    }

    public ChEBIWebServiceConnection() {
        this.init();
    }

    public void setMaxResults(int maxRes) {
        this.maxResultsSearch = maxRes;
    }

    @Override
    public HashMap<String, String> getInChIs(String[] ids) {
        HashMap<String, String> inchis = new HashMap<String, String>();
        try {
            for (String id : ids) {
                Entity entity;
                entity = this.client.getCompleteEntity("CHEBI:" + id);
                String inchiStr = entity.getInchi();
                if (inchiStr != null) {
                    inchis.put("CHEBI:" + id, inchiStr);
                }
            }
        } catch (ChebiWebServiceFault_Exception e) {
            logger.error("Problems getting complete entity", e);
        }
        return inchis;
    }

// * Rather use the inherited method if we are not downloading inchikeys from ChEBI
// * 
// * public HashMap<String, String> getInChIKeys(String[] ids) throws Exception {
//		// this method should be producing an inchi key from the database, not calculated by us
//		HashMap<String, String> inchisMap = this.getInChIs(ids);
//		HashMap<String, String> inchiKeys = new HashMap<String, String>();
//		InChIGeneratorFactory factory = new InChIGeneratorFactory();
//		DefaultChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
//		// Get InChIToStructure
//		for(String id : inchisMap.keySet()) {
//			InChIToStructure intostruct = factory.getInChIToStructure(inchisMap.get(id),builder);
//			InChIGenerator inchiGen = factory.getInChIGenerator(intostruct.getAtomContainer());
//			inchiKeys.put(id, inchiGen.getInchiKey());
//		}
//		return inchiKeys;
//	}
    @Override
    public boolean downloadStructureFiles(String[] ids, String path) {
        BufferedWriter struc = null;
        try {
            for (String id : ids) {
                Entity entity = this.client.getCompleteEntity("CHEBI:" + id);
                List<StructureDataItem> s = entity.getChemicalStructures();
                struc = new BufferedWriter(new FileWriter(path + "/ChEBI/CHEBI:" + id + ".sdf"));
                for (StructureDataItem st : s) {
                    struc.write(st.getStructure());
                }
                struc.close();
            }
        } catch (ChebiWebServiceFault_Exception e) {
            logger.error("Problems getting complete Entity", e);
            return false;
        } catch (IOException e) {
            logger.error("Problems writing sdf file", e);
            return false;
        }
        return true;
    }

    public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
        ArrayList<IAtomContainer> res = new ArrayList<IAtomContainer>();
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        try {
            for (String id : ids) {
                Entity entity = this.client.getCompleteEntity("CHEBI:" + id);
                List<StructureDataItem> s = entity.getChemicalStructures();
                for (StructureDataItem st : s) {
                    //System.out.println("CHEBI:"+id);
                    MDLV2000Reader r = new MDLV2000Reader(new StringReader(st.getStructure()));
                    //System.out.println("Before reading mol file");
                    //System.out.println(st.getStructure());
                    IAtomContainer mol = (IMolecule) r.read(builder.newInstance(IMolecule.class));
                    r.close();
                    //System.out.println("CHEBI:"+id);
                    mol.setID(id);
                    mol.setProperty("ChEBI_ID", "CHEBI:"+id);
                    res.add(mol);
                }
            }
        } catch (ChebiWebServiceFault_Exception e) {
            logger.error("Problem loading ChEBI complete entity", e);
        } catch (CDKException e) {
            logger.error("Problems reading from MDL reader", e);
        } catch (IOException e) {
            logger.error("Problems closing MDLReader", e);
        }
        return res;
    }

    public HashMap<String, List<LiteEntity>> getLiteEntity(String[] chebiIds) {
        HashMap<String, List<LiteEntity>> res = new HashMap<String, List<LiteEntity>>();
        try {
            for (String chebiId : chebiIds) {
                LiteEntityList entities = client.getLiteEntity(chebiId,
                        SearchCategory.CHEBI_ID, 1, StarsCategory.ALL);
                List<LiteEntity> resultList = entities.getListElement();
                if (resultList != null) {
                    res.put(chebiId, resultList);
                }
            }
            return res;

        } catch (ChebiWebServiceFault_Exception e) {
            logger.error("Problem loading ChEBI lite entity list.", e);
            return null;
        }
    }

    public ArrayList<Entity> getCompleteEntities(ArrayList<String> chebiIds) {
        ArrayList<Entity> res = new ArrayList<Entity>();
        try {
            for (String id : chebiIds) {
                if(id.contains("CHEBI:"))
                    id = id.replaceAll("CHEBI:", "");
                Entity entity = client.getCompleteEntity("CHEBI:" + id);
                res.add(entity);
            }

        } catch (ChebiWebServiceFault_Exception e) {
            logger.error("Problem loading ChEBI complete entity", e);
        }
        return res;
    }

    public String getServiceProviderName() {
        return this.serviceProviderName;
    }

    @Override
    public HashMap<String, String> searchByInChI(String inchi) {
        return this.searchBy(inchi, SearchCategory.INCHI_INCHI_KEY);
    }

    public HashMap<String, String> searchByName(String name) {
        return this.searchBy(name, SearchCategory.CHEBI_NAME);
    }

    public HashMap<String, String> search(String search) {
        return this.searchBy(search, SearchCategory.ALL);
    }

    public HashMap<String, String> searchBySynonym(String syn) {
        return this.searchBy(syn, SearchCategory.ALL_NAMES);
    }

    public HashMap<String, Float> searchBySmiles(String syn) {
        return this.searchBestBy(syn, SearchCategory.SMILES);
    }

    public HashMap<String, String> searchByIupacName(String iupacName) {
        return this.searchBy(iupacName, SearchCategory.IUPAC_NAME);
    }

    private HashMap<String, String> searchBy(String name, SearchCategory a) {
        HashMap<String, String> res = new HashMap<String, String>();
        try {
            LiteEntityList ents = client.getLiteEntity(name, a, 200,StarsCategory.ALL);
            List<LiteEntity> listMols = ents.getListElement();
            for (LiteEntity leMol : listMols) {
                res.put(leMol.getChebiId(), leMol.getChebiAsciiName());
                //res.put(leMol.getChebiId(), leMol.getSearchScore());
            }

        } catch (ChebiWebServiceFault_Exception ex) {
            logger.error("Problems getting lite entity from ChEBI Web service", ex);
        }
        return res;
    }

    private HashMap<String, Float> searchBestBy(String name, SearchCategory a) {
        HashMap<String, Float> res = new HashMap<String, Float>();
        try {
            LiteEntityList ents = client.getLiteEntity(name, a, 1,StarsCategory.ALL);
            List<LiteEntity> listMols = ents.getListElement();
            for (LiteEntity leMol : listMols) {
                res.put(leMol.getChebiId(), leMol.getSearchScore());
                //res.put(leMol.getChebiId(), leMol.getSearchScore());
            }

        } catch (ChebiWebServiceFault_Exception ex) {
            logger.error("Problems getting lite entity from ChEBI Web service", ex);
        }
        return res;
    }

    public HashMap<String, Float> similaritySearch(String mol, Float tanimotoCutOff) {
        HashMap<String,Float> res = new HashMap<String, Float>();
        try {
            LiteEntityList ents = client.getStructureSearch(mol, StructureType.MOLFILE, StructureSearchCategory.SIMILARITY, this.maxResultsSearch, tanimotoCutOff);
            List<LiteEntity> listMols = ents.getListElement();
            for(LiteEntity leMol : listMols) {
                res.put(leMol.getChebiId(), leMol.getSearchScore());
            }
        } catch( ChebiWebServiceFault_Exception ex) {
            logger.error("Problems with structure search",ex);
        }
        return res;
    }

    public HashMap<String, Float> identitySearch(String mol, Float tanimotoCutOff) {
        HashMap<String,Float> res = new HashMap<String, Float>();
        try {
            LiteEntityList ents = client.getStructureSearch(mol, StructureType.MOLFILE, StructureSearchCategory.IDENTITY, this.maxResultsSearch, tanimotoCutOff);
            List<LiteEntity> listMols = ents.getListElement();
            for(LiteEntity leMol : listMols) {
                res.put(leMol.getChebiId(), leMol.getSearchScore());
            }
        } catch( ChebiWebServiceFault_Exception ex) {
            logger.error("Problems with structure search",ex);
        }
        return res;
    }

    public Set<String> getNamesAndSynonyms(String chebiID) {
        try {
            Set<String> synsAndNames = new HashSet<String>();
            Entity entity = client.getCompleteEntity(chebiID);
            List<DataItem> syns = entity.getSynonyms();
            for (DataItem dataItem : syns) {
                synsAndNames.add(dataItem.getData());
            }
            for (DataItem dataItem : entity.getIupacNames()) {
                synsAndNames.add(dataItem.getData());
            }
            synsAndNames.add(entity.getChebiAsciiName());
            return synsAndNames;
        } catch(ChebiWebServiceFault_Exception ex) {
            logger.error("Problem with chebi search",ex);
        }
        return null;
    }


    /*
     *
     */
    // CDK fucntionality should be rather decorated instead of built in.
    public HashMap<String, Float> similaritySearch(IAtomContainer mol, Float tanimotoCutOff) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MDLV2000Writer writer = new MDLV2000Writer(baos);
        try {
            writer.writeMolecule(mol);
        } catch(Exception e) {
            logger.error("Problems producing mol string for CDK mol", e);
            return null;
        }
        try {
            writer.close();
        } catch (IOException ex) {
            
        }
        return this.similaritySearch(baos.toString(), tanimotoCutOff);
    }

    public HashMap<String, Float> identitySearch(IAtomContainer mol, Float tanimotoCutOff) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MDLV2000Writer writer = new MDLV2000Writer(baos);
        try {
            writer.writeMolecule(mol);
        } catch(Exception e) {
            logger.error("Problems producing mol string for CDK mol", e);
            return null;
        }
        try {
            writer.close();
        } catch (IOException ex) {
            
        }
        return this.identitySearch(baos.toString(), tanimotoCutOff);
    }
}