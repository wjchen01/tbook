package org.wjchen.textbook.web.panels;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.web.pages.MyTextbookPage;

public class ImportFromFilePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	CourseBookService coursebookService;
	
    private class FileUploadForm extends Form<Void>
    {
    	
		private static final long serialVersionUID = 1L;

		FileUploadField fileUploadField;

        public FileUploadForm(String name)
        {
            super(name);

            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            //
            add(new Label("textboook-upload-description", new ResourceModel("import.upload.textbook.description")));
            
            // Add one file input field
            fileUploadField = new FileUploadField("textbook-upload-input");
            add(fileUploadField);
            
            // Add download template
            add(new Label("textbook-download-description", new ResourceModel("import.upload.textbook.template")));
            File file = new File(getClass().getResource("/Textbook_Template.xls").getFile());
            DownloadLink download_link = new DownloadLink("textbook-download-template-btn", file, "Textbook_Template.xls");
            download_link.add(new Label("textbook-download-btn-label", new ResourceModel("import.upload.textbook.download")));
            add(download_link);            
            
            // Add "Import button
            Button upload_btn = new Button("textbook-upload-btn") {

				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
		            final List<FileUpload> uploads = fileUploadField.getFileUploads();
		            if (uploads != null) {
		                for (FileUpload upload : uploads) {
							try {
								InputStream xlsxStream = upload.getInputStream();
								String userNm = TextbookSession.get().getUserNm();
								String siteNm = TextbookSession.get().getSiteNm();
								
								coursebookService.loadCourseBooks(siteNm, userNm, xlsxStream);
								
								xlsxStream.close();
							} 
							catch (IOException e) {
								e.printStackTrace();
							}
		                }		                

		                getRequestCycle().setResponsePage(MyTextbookPage.class);
		            }	
				}
            	
            };
            upload_btn.add(new Label("textbook-upload-btn-label", new ResourceModel("import.upload.textbook.import")));
            add(upload_btn);
         
            // Add "Cancel" button
            Button cancel_btn = new Button("textbook-cancel-btn") {

				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					getRequestCycle().setResponsePage(MyTextbookPage.class);	
				}
            	
            };
            cancel_btn.add(new Label("textbook-cancel-btn-label", new ResourceModel("import.upload.textbook.cancel")));
            add(cancel_btn);
        }

    }

	public ImportFromFilePanel(String id) {
		super(id);

		 final FeedbackPanel uploadFeedback = new FeedbackPanel("textbook-upload-feedback");
		 add(uploadFeedback);
		 
		 final FileUploadForm uploadForm = new FileUploadForm("textbook-upload-form");
	     add(uploadForm);
	     
	}

}
