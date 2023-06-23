package pe.edu.pucp.tel131lab9.dao;

import pe.edu.pucp.tel131lab9.bean.Employee;
import pe.edu.pucp.tel131lab9.bean.Post;
import pe.edu.pucp.tel131lab9.dto.PostDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostDao extends DaoBase{

    public ArrayList<Post> listPosts() {

        ArrayList<Post> posts = new ArrayList<>();

        String sql = "SELECT p.post_id, p.title, p.content, e.employee_id, p.datetime, e.first_name, e.last_name, COUNT(c.post_id)\n" +
                "FROM post p \n" +
                "left join employees e on e.employee_id = p.employee_id\n" +
                "left join comments c on c.post_id = p.post_id\n" +
                "group by p.post_id;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Post post = new Post();
                fetchPostData(post, rs);
                posts.add(post);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return posts;
    }

    public Post getPost(int id) {

        Post post = null;

        String sql = "SELECT * FROM post p left join employees e on p.employee_id = e.employee_id "+
                "where p.post_id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    post = new Post();
                    fetchPostData(post, rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return post;
    }

    public Post savePost(Post post) {

        return post;
    }
    public ArrayList<PostDTO> listcomments() {

        ArrayList<PostDTO> listcomments = new ArrayList<>();

        String sql = "SELECT COUNT(c.post_id) as 'cant_post',c.post_id FROM comments c inner join post p on c.post_id = p.post_id GROUP BY c.post_id;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PostDTO postdto = new PostDTO();
                fetchPostDTO(postdto, rs);
                listcomments.add(postdto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listcomments;
    }

    private void fetchPostDTO(PostDTO postdto, ResultSet rs) throws SQLException {
        postdto.setCant_post(rs.getInt(1));
        postdto.setPost_id(rs.getInt(2));
    }
    private void fetchPostData(Post post, ResultSet rs) throws SQLException {
        post.setPostId(rs.getInt("post_id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setEmployeeId(rs.getInt(4));
        post.setDatetime(rs.getTimestamp(5));

        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("e.employee_id"));
        employee.setFirstName(rs.getString("e.first_name"));
        employee.setLastName(rs.getString("e.last_name"));
        post.setEmployee(employee);
    }

}
