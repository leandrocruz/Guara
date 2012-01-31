package br.com.ibnetwork.guara.app;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import br.com.ibnetwork.guara.app.file.FileRepository;
import br.com.ibnetwork.guara.app.test.SampleBean;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class FileRepositoryTest
    extends XinguTestCase
{
	@Inject
	protected FileRepository repository;

	@Test
	public void testAppRoot()
		throws Exception
	{
		File root = repository.getRootDir();
		assertEquals(true,root.isDirectory());
	}
	
	@Test
	public void testTargetDir()
		throws Exception
	{
		SampleBean bean = new SampleBean();
		String dir; 
		
		dir = repository.getTargetDir(bean, "x");
		assertEquals(FileRepository.UGLY+"/guara/app/test/samplebean/0/x",dir);
		
		bean.setId(1);
		dir = repository.getTargetDir(bean, "x");
		assertEquals(FileRepository.UGLY+"/guara/app/test/samplebean/1/x",dir);

		dir = repository.getTargetDir(new Pojo(), "x");
		assertEquals(FileRepository.UGLY+"/guara/app/pojo/x",dir);

		dir = repository.getTargetDir(new Pojo(), null);
		assertEquals(FileRepository.UGLY+"/guara/app/pojo",dir);

		dir = repository.getTargetDir(null, "x");
		assertEquals(null,dir);
	}
}

class Pojo {}
