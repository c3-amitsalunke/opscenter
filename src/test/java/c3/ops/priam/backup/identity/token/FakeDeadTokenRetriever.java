package c3.ops.priam.backup.identity.token;

import c3.ops.priam.identity.PriamInstance;
import c3.ops.priam.identity.token.IDeadTokenRetriever;
import com.google.common.collect.ListMultimap;

public class FakeDeadTokenRetriever implements IDeadTokenRetriever {

  @Override
  public PriamInstance get() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getReplaceIp() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLocMap(ListMultimap<String, PriamInstance> locMap) {
    // TODO Auto-generated method stub

  }

}
