package dao;

import java.util.List;
import model.AuthToken;

public interface AuthTokenDao {
    public List<AuthToken> getAllAuthTokens();
    public AuthToken getAuthTokenById(Integer id);
    public Integer addAuthToken(AuthToken authToken);
    public Integer deleteAuthToken(Integer id);
    public Integer modifyAuthToken(AuthToken authToken);
    public String getTokenByIpAgentAndUserId(String ip, String agent, Integer userId);
    public Integer deleteAuthTokensByIpAgentAndUserId(String ip, String agent, Integer userId);
}