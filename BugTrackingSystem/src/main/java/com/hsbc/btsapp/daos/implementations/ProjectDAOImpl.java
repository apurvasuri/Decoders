package com.hsbc.btsapp.daos.implementations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hsbc.btsapp.beans.Project;
import com.hsbc.btsapp.beans.enums.Status;
import com.hsbc.btsapp.daos.interfaces.ProjectDAO;
import com.hsbc.btsapp.exceptions.ProjectAlreadyExistsException;
import com.hsbc.btsapp.exceptions.ProjectDoesNotExistException;
import com.hsbc.btsapp.utils.ConnectionUtils;

public class ProjectDAOImpl implements ProjectDAO{

	private PreparedStatement pst_1;
	@Override
	public void addProject(Project project) throws ProjectAlreadyExistsException {
		String project_count = "select count(project_id) from Project where project_id=" +project.getProjectId();
		try {
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement(project_count);
			ResultSet rs = pst.executeQuery();
			int a = 0;
			if(rs.next()) {
				
				a = rs.getInt(1);
			}
			
			if(a==1) {
				throw new ProjectAlreadyExistsException();
			}
			else {
				PreparedStatement pst_1 = conn.prepareStatement("insert into Project(project_id, project_name, project_description, project_start_date, "
						+ "project_status value(?,?,?,?,?");
				pst_1.setInt(1, project.getProjectId());
				pst_1.setString(2, project.getProjectName());
				pst_1.setString(3, project.getProjectDescription());
				pst_1.setDate(4, project.getProjectStartDate());
				pst_1.setString(5, project.getProjectStatus().toString());
				int count = pst_1.executeUpdate();
				if (count == 1)
				{
					System.out.println("Project added");
				}
			}
	}catch(SQLException e) {
		e.printStackTrace();
	}
		finally {
			ConnectionUtils.closeConnection();
		}
	}

	@Override
	public void updateProject(Project project) {
		String project_count = "select count(project_count) from Project where project_id=" + project.getProjectId();
		try {
			Connection conn=ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement(project_count);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				System.out.println("Project not present to update");
			}
			else {
				PreparedStatement pst_1 = conn.prepareStatement("update Project set project_name=?, set project_description=?, set project_start_date=?,set project_status=?,"
						+ " where project_id=?");
				pst_1.setString(1, project.getProjectName());
				pst_1.setString(2, project.getProjectDescription());
				pst_1.setDate(3, project.getProjectStartDate());
				pst_1.setString(4, project.getProjectStatus().toString());
				pst_1.setDouble(5, project.getProjectId());
				int count = pst_1.executeUpdate();
				if (count == 1)
				{
					System.out.println("Project Updated");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionUtils.closeConnection();
		}	
	}

	@Override
	public Project getProjectById(int projectId) throws ProjectDoesNotExistException{
		Project project = null;
		try 
		{
			Connection conn=ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from Project where project_id=?");
			pst.setInt(1, projectId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) 
			{
				project = new Project(rs.getInt("project_id"), rs.getString("project_name"),rs.getString("project_description"),
						Date.valueOf(rs.getString("project_start_date")), Status.valueOf(rs.getString("project_status")), rs.getInt("team_id"));
			} 
			else
			{
				throw new ProjectDoesNotExistException();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally {
			ConnectionUtils.closeConnection();
        }
		return project;
		
	}

	@Override
	public Project getProjectByName(String projectName) {
		Project project=null;
		try {
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from Project where project_name");
			pst.setString(1, projectName);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				project = new Project(rs.getInt("project_id"),rs.getString("project_name"),rs.getString("project_description"),
						Date.valueOf(rs.getString("project_start_date")),Status.valueOf(rs.getString("project_status")),rs.getInt("team_id"));
			}
			else {
				System.out.println("Project not found");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionUtils.closeConnection();
		}
		return project;
		
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projectList= new ArrayList<Project>();
		try
		{
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from Project");
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				projectList.add(new Project(rs.getInt("project_id"), rs.getString("project_name"), rs.getString("project_description"),
						Date.valueOf(rs.getString("project_start_date")),Status.valueOf(rs.getString("project_status")),rs.getInt("team_id")));
			} 
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally {
			ConnectionUtils.closeConnection();
        }
		return projectList;
	}

	@Override
	public void deleteProject(Project project) throws ProjectDoesNotExistException{
		String project_count = "select count(project_id) from Project where project_id=" + project.getProjectId();
		try {
			Connection conn=ConnectionUtils.getConnection();
			PreparedStatement pst = conn.prepareStatement(project_count);
			ResultSet rs = pst.executeQuery();
			int a = 0;
			if (rs.next())
			{
				a = rs.getInt(1);
			}
			if (a==0)
			{
				throw new ProjectDoesNotExistException();
			}
			else
			{
				PreparedStatement pst_1 = conn.prepareStatement("delete from Project where project_id=?");
				pst_1.setInt(1, project.getProjectId());
				int count = pst_1.executeUpdate();
				if (count == 1)
				{
					System.out.println("Project Deleted");
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally {
			ConnectionUtils.closeConnection();
        }
	}
}
		
	
