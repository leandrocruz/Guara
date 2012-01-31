package br.com.ibnetwork.guara.app.xingu.pull;

import br.com.ibnetwork.guara.app.file.FileRepository;
import br.com.ibnetwork.guara.pull.impl.ApplicationToolSupport;
import br.com.ibnetwork.xingu.container.Inject;

public class FileRepositoryTool
    extends ApplicationToolSupport
{
	@Inject
	private FileRepository repository;
	
	public String getTargetDir(Object bean,String dirName)
	{
		return repository.getTargetDir(bean, dirName);
	}
}
