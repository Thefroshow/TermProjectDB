package main.javaSrc.DBHelpers.Managers;

import main.javaSrc.DBHelpers.ObjectLayer;
import main.javaSrc.Entities.Candidate;
import main.javaSrc.Entities.Election;
import main.javaSrc.Entities.EntityImpl.CandidateImpl;
import main.javaSrc.Entities.Election;
import main.javaSrc.Entities.EntityImpl.PoliticalPartyImpl;
import main.javaSrc.Entities.PoliticalParty;
import main.javaSrc.helpers.EVException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/2/2016.
 */
public class CandidateManager extends Manager{

    public CandidateManager(Connection conn, ObjectLayer objectLayer){
        super(conn,objectLayer);
    }

    public CandidateManager() {

    }

    public List<Candidate> restore(Candidate candidate) throws EVException {

        Statement    stmt = null;
        String query = "";
        List<Candidate>   candidates = new ArrayList<Candidate>();

        if( candidate != null ) {

            query = candidate.getRestoreString();

        }

        try {

            stmt = conn.createStatement();

            // retrieve the persistent candidate objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result

                int candidateId;
                String name;
                int partyId;
                int voteCount;
                Candidate nextCandidate = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved candidates
                while( rs.next() ) {

                    candidateId = rs.getInt( 1 );
                    name = rs.getString( 2 );
                    partyId = rs.getInt( 3 );
                    voteCount = rs.getInt( 4 );

                    nextCandidate = objectLayer.createCandidate(); // create a proxy candidate object
                    // and now set its retrieved attributes
                    nextCandidate.setId( candidateId );
                    nextCandidate.setName( name );

                    //This part creates a new PoliticalParty as a model **PoliticalParty still needs to be implemented**
                    //then checks the objectLayer to see if it can find a party with that partyId
                    //if so, then assigns the candidates party to be that found party
                    if(partyId >= 0) {
                        PoliticalParty p = new PoliticalPartyImpl();
                        p.setId(partyId);
                        PoliticalParty partyToSet = objectLayer.findPoliticalParty( p ).get(0);
                        if(partyToSet != null)
                            nextCandidate.setPoliticalParty(partyToSet);
                    }

                    nextCandidate.setVoteCount( voteCount );

                    candidates.add( nextCandidate );
                }

                return candidates;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "candidateManager.restore: Could not restore persistent candidate objects; Root cause: " + e );
        }

        throw new EVException( "candidateManager.restore: Could not restore persistent candidate objects" );

    }

    public Candidate store(Candidate candidate) throws EVException{
        String insertCandidate = "insert into Candidate ( Candidate_Name, Party_ID, Vote_Count) values ( ?, ?, ? )";
        String updateCandidate = "update Candidate set Candidate_Name = ?, Party_ID = ?, Vote_Count = ?";
        PreparedStatement stmt = null;
        int queryExecution;
        int candidateId;

        try {

            if( !candidate.isPersistent() ) {
                stmt = conn.prepareStatement(insertCandidate);
            }else {
                stmt = conn.prepareStatement(updateCandidate);
            }

            stmt = candidate.insertStoreData(stmt);

            queryExecution = stmt.executeUpdate();

            if( !candidate.isPersistent() ) {
                if( queryExecution >= 1 ) {
                    candidate =(Candidate) setId(stmt,candidate);
                }
                else
                    throw new EVException( "CandidateManager.save: failed to save a candidate" );
            }
            else {
                if( queryExecution < 1 )
                    throw new EVException( "CandidateManager.save: failed to save a candidate" );
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "CandidateManager.save: failed to save a candidate: " + e );
        }

        return candidate;
    }

    public void store(Candidate candidate, Election election)throws EVException{
        String insertElectionCandidates = "insert into Election_Candidates (Candidate_ID, Election_ID) values (?, ?)";
        PreparedStatement stmt = null;
        int queryExecution;

        try {
            stmt = conn.prepareStatement( insertElectionCandidates );
            if(candidate.getId() > 0)
                stmt.setInt(1, candidate.getId());
            else
                throw new EVException("CandidateManager.save can't save a candidate: Candidate ID undefined") ;

            if(election.getId() > 0)
                stmt.setInt(2, election.getId());
            else
                throw new EVException("CandidateManager.save can't save a election: Election ID undefined") ;

            queryExecution = stmt.executeUpdate();

            if(queryExecution < 1)
                throw new EVException("CandidateManager.save failed to save Election_Candidates");

        }
        catch(SQLException e){
            e.printStackTrace();
            throw new EVException("CandidateManager.store failed to save a Election_Candidates" +e);
        }

    }

    public Election restoreElectionCandidate (Candidate candidate) throws EVException{
        StringBuffer query = new StringBuffer(500);
        Statement stmt = null;

        if(candidate.getId() <1)
            throw new EVException("CandidateManager.restore could not restore persistent Election_Candidates");

        query.append("select Election.Election_ID, Election.Office_Name, Election.Is_Partisan, Election.Vote_Count ");
        query.append(" from Election ");
        query.append("join Election_Candidates");
        query.append("on Election.Election_ID = Election_Candidates.Election_ID");
        query.append("Where Election_Candidates.Candidate_ID = '" + candidate.getId() + "'");

        try{
            stmt = conn.createStatement();

            if(stmt.execute(query.toString())) {
                int electionID;
                String officeName;
                boolean isPartisan;
                int voteCount;
                Election newElection = null;

                ResultSet rs = stmt.getResultSet();

                while(rs.next()){
                    electionID = rs.getInt( 1 );
                    officeName = rs.getString( 2 );
                    isPartisan = rs.getBoolean( 3 );
                    voteCount = rs.getInt(4);

                    newElection = objectLayer.createElection();
                    newElection.setId( electionID );
                    newElection.setOffice( officeName );
                    newElection.setIsPartisan( isPartisan );
                    newElection.setVoteCount(voteCount);
                    break;
                }
                return newElection;
            }
        }

        catch(SQLException e){
            e.printStackTrace();
            throw new EVException("BallotManager.store failed to save a ballot_issue" +e);
        }
        throw new EVException("BallotManager.restore could not restore ballot object");
    }

    public List<Candidate> restore (Election election) throws EVException{
        StringBuffer query = new StringBuffer(500);
        Statement stmt = null;
        List<Candidate> candidates = new ArrayList<Candidate>();

        if(election.getId() <1)
            throw new EVException("CandidateManger.restore could not restore persistent Candidate_Elections");

        query.append("select Candidate.Candidate_Name, Candidate.Vote_Count");
        query.append(" from Candidate ");
        query.append("join Election_Candidates");
        query.append("on Election.Election_ID = Election_Candidates.Election_ID");
        query.append("Where Election.Election_ID = '" + election.getId() + "'");

        try {
            stmt = conn.createStatement();

            if (stmt.execute(query.toString())) { // statement returned a result

                String candidateName;
                int voteCount;


                Candidate newCandidate = null;

                ResultSet rs = stmt.getResultSet();


                while (rs.next()) {

                    candidateName = rs.getString(1);
                    voteCount = rs.getInt(2);

                    newCandidate = new CandidateImpl();
                    newCandidate.setName(candidateName);
                    newCandidate.setVoteCount(voteCount);

                    candidates.add(newCandidate);
                }

                return candidates;
            }
        }

        catch(SQLException e){
            e.printStackTrace();
            throw new EVException("BallotManager.store failed to save a ballot_issue" +e);
        }
        throw new EVException("BallotManager.restore could not restore ballot object");
    }

    public void delete(Candidate candidate, Election election) throws EVException{
        String               deleteCandidate = "delete from Election_Candidates where Candidate_ID = ? and Election_ID = ?";
        PreparedStatement    stmt = null;
        int                  queryExecution;

        try{
            stmt = conn.prepareStatement( deleteCandidate );
            if(candidate.getId() >0)
                stmt.setInt(1, candidate.getId());
            else
                throw new EVException("CandidateManager.delete failed to delete Candidate");
            if(election.getId() > 0)
                stmt.setInt(2, election.getId());
            else
                throw new EVException("CandidateManager.delete failed to delete Candidate");
            queryExecution = stmt.executeUpdate();
            if(queryExecution != 1)
                throw new EVException("CandidateManager.delete failed to delete");
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "CandidateManger.delete: failed to delete a candidate: " + e );
        }
    }

    public void delete(Candidate candidate) throws EVException {

        String               deleteCandidate = "delete from Candidate where Candidate_ID = ?";
        PreparedStatement    stmt = null;
        int                  queryExecution;

        if( !candidate.isPersistent() ) // is the candidate object persistent?  If not, nothing to actually delete
            return;

        try {
            stmt = conn.prepareStatement( deleteCandidate );
            stmt.setInt( 1, candidate.getId() );
            queryExecution = stmt.executeUpdate();
            if( queryExecution == 1 ) {
                return;
            }
            else
                throw new EVException( "candidateManager.delete: failed to delete a candidate" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "candidateManager.delete: failed to delete a candidate: " + e );        }
    }


}

