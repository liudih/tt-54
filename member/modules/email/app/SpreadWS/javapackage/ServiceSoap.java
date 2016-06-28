/**
 * ServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public interface ServiceSoap extends java.rmi.Remote {

    /**
     * GetUploadRequestStatus<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>RequestID: The request of SpreadWebServcie.UploadContactListFile
     * return .<br>return dataset.<br><br>Dataset Columns:<br>RequestID,
     * UploadStatus(Complete or Error or Uploading), ContactList, UploadTime,
     * TotalCount, NewCount, DuplicateCount,DeletedCount, ExistingCount,
     * SpamCount, UnsubscribedCount, DoNotMailCount, UndeliverableCount,
     * asUnconfirmedCount, InvalidCount
     */
    public SpreadWS.javapackage.GetUploadRequestStatusResponseGetUploadRequestStatusResult getUploadRequestStatus(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String requestID) throws java.rmi.RemoteException;

    /**
     * UnsubscribeEmailInAccount<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>subscriberEmail: The email of subscriber.<br>return
     * true if success.<br><br>
     */
    public boolean unsubscribeEmailInAccount(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String subscriberEmail) throws java.rmi.RemoteException;

    /**
     * UnsubscribeEmailInContactList<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>subscriberEmail: The email of subscriber.<br>contactListName:
     * The name of contactlist.<br>return true if success.<br><br>
     */
    public boolean unsubscribeEmailInContactList(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String subscriberEmail, java.lang.String contactListName) throws java.rmi.RemoteException;

    /**
     * Create a new email campaign draft.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>campaignArgs:
     * Campaign Class, includes campaignName, fromEmail, from, subject, content,
     * signature and schedule<br>category: An array of string contains the
     * names of categories added to this email campaign.<br>interval: Type
     * of email Campaign. -1, send for once only. Non-negetive number, n
     * days after subscribers subscribe.<br><br>return campaignID.
     */
    public int createCampaignDraft(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.Campaign campaignArgs, java.lang.String[] category, int interval) throws java.rmi.RemoteException;

    /**
     * Create a new email campaign and send it out.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>campaignArgs:
     * Campaign Class, includes campaignName, fromEmail, from, subject, content,
     * signature and schedule<br>category: An array of string contains the
     * names of categories added to this email campaign.<br>interval: Type
     * of email Campaign. -1, send for once only. Non-negetive number, n
     * days after subscribers subscribe.<br><br>return campaignID.
     */
    public int createCampaign(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.Campaign campaignArgs, java.lang.String[] category, int interval) throws java.rmi.RemoteException;

    /**
     * Create a new email campaign and campaign creatives .<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>campaignName:
     * The name of the email campaign.<br>campaignCreatives: campaignCreatives
     * Array, CampaignCreatives Class includes displayName, fromAddress,
     * replyTo, subject, creativeContent, target and isCampaignDefault.<br>category:
     * An array of string contains the names of categories added to this
     * email campaign.<br>interval: Type of email Campaign. -1, send for
     * once only. Non-negetive number, n days after subscribers subscribe.<br><br>schedule:
     * The date of when to establish the email campaign.<br>signature: The
     * signature of the email campaign.<br>campaignStatus: The status of
     * the email campaign.<br>return campaignID.
     */
    public int createCampaign2(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignName, SpreadWS.javapackage.CampaignCreatives[] campaignCreatives, java.lang.String[] category, int interval, java.util.Calendar schedule, java.lang.String signature, SpreadWS.javapackage.CampaignStatus campaignStatus) throws java.rmi.RemoteException;

    /**
     * Create a new email campaign and campaign creatives .<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>campaignID:
     * The ID of the email campaign.<br>campaignStatus: The status of the
     * email campaign.<br>return true if succeed.
     */
    public boolean updateCampaignStatus(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignID, SpreadWS.javapackage.CampaignStatus campaignStatus) throws java.rmi.RemoteException;

    /**
     * Create a new sms campaign <br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: The APIKey of your Spread account
     * which you can retrieve from your Spread account (My account=> Settings).<br>SMSContent:
     * The content of SMS campaign.<br>sender:The sender of SMS campaign.<br>schedule:
     * The date of when to establish the email campaign.<br>campaignStatus:
     * The status of the email campaign.<br>phoneSubscribers: An array of
     * string contains the phone number added to this email campaign.<br>category:
     * An array of string contains the names of categories added to this
     * email campaign.<br>return campaignID.
     */
    public int sendSMS(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String SMSContent, java.lang.String sender, java.util.Calendar schedule, SpreadWS.javapackage.CampaignStatus campaignStatus, java.lang.String[] phoneSubscribers, java.lang.String[] category) throws java.rmi.RemoteException;

    /**
     * Get Sms Replies <br>Arguments:<br><br>loginEmail: Login Email
     * of Spread.<br>APIKey: The APIKey of your Spread account which you
     * can retrieve from your Spread account (My account=> Settings).<br>campaignId:
     * Campaign Id<br>return table, column: phone\content\time.
     */
    public SpreadWS.javapackage.GetSmsRepliesResponseGetSmsRepliesResult getSmsReplies(java.lang.String loginEmail, java.lang.String password, int campaignId) throws java.rmi.RemoteException;

    /**
     * Get the Status of an Email Campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>CampaignID<br><br>return
     * Status.
     */
    public SpreadWS.javapackage.CampaignStatus getCampaignStatus(java.lang.String loginEmail, java.lang.String password, int campaignID) throws java.rmi.RemoteException;

    /**
     * Get the Report of an Email Campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>CampaignID<br><br>return
     * CampaignReport.
     */
    public SpreadWS.javapackage.CampaignReport getCampaignReport(java.lang.String loginEmail, java.lang.String password, int campaignID) throws java.rmi.RemoteException;

    /**
     * Publish or not publish Newsletters
     */
    public void changePublishStatus(java.lang.String loginEmail, java.lang.String password, java.lang.String campaign_id, boolean isPublish) throws java.rmi.RemoteException;

    /**
     * Get all subscriptions.<br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>password: Login Password of Spread.<br><br>return
     * dataset.<br><br>Dataset Columns:<br>Subscription Name, description,
     * status, no. of subscribers
     */
    public SpreadWS.javapackage.GetAllSubscriptionResponseGetAllSubscriptionResult getAllSubscription(java.lang.String loginEmail, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Get subscriptions in particular Status.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>status<br><br>return
     * dataset.<br><br>Dataset Columns:<br>Subscription Name, description,
     * status, no. of subscribers
     */
    public SpreadWS.javapackage.GetSubscriptionResponseGetSubscriptionResult getSubscription(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.SubscriptionStatus status) throws java.rmi.RemoteException;

    /**
     * Get subscriptions in particular Status.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>status<br><br>return
     * string.<br><br>
     */
    public java.lang.String getSubscriptions2String(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.SubscriptionStatus status) throws java.rmi.RemoteException;

    /**
     * Get specified status's subscribers in subscription.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>subscription:
     * Subscription Name<br>status: subscriber status<br><br>return dataset.<br><br>Dataset
     * Columns:<br>subscriber_email,Status
     */
    public SpreadWS.javapackage.GetSubscribersResponseGetSubscribersResult getSubscribers(java.lang.String loginEmail, java.lang.String password, java.lang.String subscription, SpreadWS.javapackage.SubscriberStatus status) throws java.rmi.RemoteException;

    /**
     * Create a new subscription.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>subscriptionName:
     * Subscription Name<br>description: Description of the new subscription.<br><br>return
     * true if succeed.
     */
    public boolean createSubscription(java.lang.String loginEmail, java.lang.String password, java.lang.String subscriptionName, java.lang.String description) throws java.rmi.RemoteException;

    /**
     * Create a new Contact List.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>subscriptionName:
     * Subscription Name<br>description: Description of the new subscription.<br><br>return
     * true if succeed.
     */
    public boolean createContactList(java.lang.String loginEmail, java.lang.String password, java.lang.String subscriptionName, java.lang.String description) throws java.rmi.RemoteException;

    /**
     * Add new/existing subscriber into a subscription.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>subscriptionEmail:
     * Subscriber's email to be added.<br>subscription: The subscription
     * where the subscriber being added.<br>optInType: Double Opt-In Option.<br><br>return
     * true if the subscriber need to be confirmed.<br><br>Remarks: Subscriber
     * would receive confirmation email if user's Double Opt-in option is
     * ON.
     */
    public boolean addSubscriberByEmail(java.lang.String loginEmail, java.lang.String password, java.lang.String subscriberEmail, java.lang.String subscription, SpreadWS.javapackage.DoubleOptIn optInType) throws java.rmi.RemoteException;

    /**
     * delete existing subscribers.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>subscriberEmails:
     * Subscriber's emails to be deleted.<br>subscription: The subscription
     * which contains the emails. if subscription is not specified, emails
     * will delete from account.<br>
     */
    public int deleteSubscribers(java.lang.String loginEmail, java.lang.String password, java.lang.String[] subscriberEmails, java.lang.String subscription) throws java.rmi.RemoteException;

    /**
     * Add new/existing subscriber into a subscription.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>SubscriberArgs:
     * email, firstName, middleName, lastName, jobTitle, company, homePhone,
     * address1, address2, address3, city, state, country,postalCode, subPostalCode,
     * customField1, customField2, customField3.<br>subscription: The subscription
     * where the subscriber being added.<br>optInType: Double Opt-In Option.<br><br>return
     * true if the subscriber need to be confirmed.<br><br>Remarks: Subscriber
     * would receive confirmation email if user's Double Opt-in option is
     * ON.
     */
    public boolean addSubscriberByInfo(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.Subscriber subscriberArgs, java.lang.String subscription, SpreadWS.javapackage.DoubleOptIn optInType) throws java.rmi.RemoteException;

    /**
     * Add new/existing subscribers into a subscription.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>SubscriberArgs():
     * email, firstName, middleName, lastName, jobTitle, company, homePhone,
     * address1, address2, address3, city, state, country,postalCode, subPostalCode,
     * customField1, customField2, customField3.<br>subscription: The subscription
     * where the subscriber being added.<br>optInType: Double Opt-In Option.<br><br>return
     * dataset containing error messages if any.<br><br>Remarks: Subscriber
     * would receive confirmation email if user's Double Opt-in option is
     * ON.
     */
    public SpreadWS.javapackage.AddSubscribersByInfoResponseAddSubscribersByInfoResult addSubscribersByInfo(java.lang.String loginEmail, java.lang.String password, SpreadWS.javapackage.Subscriber[] subscriberArgs, java.lang.String subscription, SpreadWS.javapackage.DoubleOptIn optInType) throws java.rmi.RemoteException;

    /**
     * The same function as Send but you can specify campaign name
     * instead of today as default campaign name.
     */
    public java.lang.String send2(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignName, java.lang.String from, java.lang.String fromName, java.lang.String to, java.lang.String subject, java.lang.String body) throws java.rmi.RemoteException;

    /**
     * The same function as Send but you can specify campaign name
     * instead of today as default campaign name.
     */
    public java.lang.String send3(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignName, java.lang.String from, java.lang.String fromName, java.lang.String to, java.lang.String replyTo, java.lang.String subject, java.lang.String body) throws java.rmi.RemoteException;

    /**
     * Send email , return email id
     */
    public java.lang.String emailSend(java.lang.String loginEmail, java.lang.String password, java.lang.String from, java.lang.String fromName, java.lang.String to, java.lang.String subject, java.lang.String body) throws java.rmi.RemoteException;

    /**
     * Send email one to one
     */
    public java.lang.String send(java.lang.String loginEmail, java.lang.String password, java.lang.String from, java.lang.String fromName, java.lang.String to, java.lang.String subject, java.lang.String body) throws java.rmi.RemoteException;

    /**
     * Get email status(Request\Delivered\Opened\Clicked\Bounce\SPAM\Invalid)</br>Return
     * emailId$status (123-456$Opened)</br></br>LoginEmail: Login Email of
     * Spread.</br>Password: Login Password of Spread.</br>EmailIds: EmailId,
     * semicolon(;) as delimiter.
     */
    public java.lang.String[] getEmailStatus(java.lang.String loginEmail, java.lang.String password, java.lang.String emailIds) throws java.rmi.RemoteException;

    /**
     * Get SMS status (0: unsuccessfully \ 1: successfully)</br>Return
     * Phone number$status (85212345678$1)</br></br>LoginEmail: Login Email
     * of Spread.</br>Password: Login Password of Spread.</br>CampaignId:
     * Campaign id
     */
    public java.lang.String[] getSMSStatus(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignId) throws java.rmi.RemoteException;
    public java.lang.String helloWorld() throws java.rmi.RemoteException;

    /**
     * Send Batch
     */
    public SpreadWS.javapackage.Server sendBatch(java.lang.String xml) throws java.rmi.RemoteException;

    /**
     * Get if subscribers satified the conditions,<b>it is only for
     * internal use currently</b> <br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>password: Login Password of Spread.<br>Criteria:
     * A string represent the Conditions in Json Formate.<br>TopN: Quantity
     * of subscribers needed <br>SaveAsList: Name of subscription to be created.if
     * more than one list,use ";" as seperator.e.g. listA;listB;listC<br>ForceCreate:
     * TRUE or FALSE. When number of subscribers returned less than TopN,if
     * TRUE then will create new list and FALSE will not.<br><br> Return
     * the number of subscribers that meet the condition.<br><br>Remark:
     * This function is currently for internal use.
     */
    public int searchContacts(java.lang.String loginEmail, java.lang.String password, java.lang.String criteria, int topN, java.lang.String saveAsList, boolean forceCreate) throws java.rmi.RemoteException;

    /**
     * Split the contact list. <br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>password: Login Password of Spread.<br>oldListName:The
     * name of old list name <br>SaveAsList: Name of subscription to be created.if
     * more than one list,use ";" as seperator.e.g. listA;listB;listC<br><br>return
     * true if succeed.
     */
    public boolean splitContacts(java.lang.String loginEmail, java.lang.String password, java.lang.String oldListName, java.lang.String saveAsList) throws java.rmi.RemoteException;

    /**
     * Set exclude lists to a campaign. <br>Arguments:<br><br>LoginEmail:
     * Login Email of Spread.<br>Password: Login Password of Spread.<br>CampaignNamedListName:The
     * name of campaign <br>ExcludeLists: Name of contact list.if more than
     * one list,use ";" as seperator.e.g. listA;listB;listC<br><br>return
     * count of list excluded.
     */
    public int excludeContactList(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignName, java.lang.String excludeLists) throws java.rmi.RemoteException;

    /**
     * Get send report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,
     * isSuccess,sending_status_time
     */
    public SpreadWS.javapackage.GetCampaignSentsResponseGetCampaignSentsResult getCampaignSents(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get open report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,
     * email_opening_time
     */
    public SpreadWS.javapackage.GetCampaignOpensResponseGetCampaignOpensResult getCampaignOpens(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get clicks report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,date_clicked
     * ,url
     */
    public SpreadWS.javapackage.GetCampaignClicksResponseGetCampaignClicksResult getCampaignClicks(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get Unsubscribe report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,
     * date
     */
    public SpreadWS.javapackage.GetCampaignUnsubscribesResponseGetCampaignUnsubscribesResult getCampaignUnsubscribes(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get abuse report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,
     * date
     */
    public SpreadWS.javapackage.GetCampaignAbusesResponseGetCampaignAbusesResult getCampaignAbuses(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get Conversion report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,
     * conversion_time,value,type,url
     */
    public SpreadWS.javapackage.GetCampaignConversionsResponseGetCampaignConversionsResult getCampaignConversions(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Please use GetCampaignConversions instead.
     */
    public SpreadWS.javapackage.GetCampaignConvertionsResponseGetCampaignConvertionsResult getCampaignConvertions(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Get Bounces report in a period of time for a campaign.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>password: Login Password of Spread.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>subscriber_email,type_name,chkdate
     */
    public SpreadWS.javapackage.GetCampaignBouncesResponseGetCampaignBouncesResult getCampaignBounces(java.lang.String loginEmail, java.lang.String password, int campaignID, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * Add Sender Email .<br/>Arguments:<br/><br/>loginEmail: Login
     * Email of Spread.<br>password: Login Password of Spread.<br>SenderEmail:
     * New Sender Email To Add.<br>return true if success.
     */
    public boolean addSenderEmail(java.lang.String loginEmail, java.lang.String password, java.lang.String senderEmail) throws java.rmi.RemoteException;

    /**
     * Create a new account.<strong>(This method is only for reseller.)</strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>loginPassword: Login Password of Spread.<br>newAccountName:
     * New account name <br>newAccountEmail: New account email<br><br>newAccountPassword:New
     * account password<br><br>return true if succeed.
     */
    public boolean createNewAccount(java.lang.String loginEmail, java.lang.String loginPassword, java.lang.String newAccountName, java.lang.String newAccountEmail, java.lang.String newAccountPassword) throws java.rmi.RemoteException;

    /**
     * Transfer credit to another account.<strong>(This method is
     * only for reseller.)</strong><br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>loginPassword: Login Password of Spread.<br>transferToEmail:
     * Email of transfer credit to user  <br>creditCount: Total amount of
     * transfer credit<br><br>return true if succeed.
     */
    public boolean transferCredit(java.lang.String loginEmail, java.lang.String loginPassword, java.lang.String transferToEmail, int creditCount) throws java.rmi.RemoteException;

    /**
     * Upload zip files as mail contents.<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>loginPassword: Login Password of Spread.<br>fileStream:
     * The zip file stream  <br>campaignId: Campaign ID.If campaignid is
     * nothing,create new campaign.<br><br>return true if succeed.
     */
    public boolean uplodeZipFile(java.lang.String loginEmail, java.lang.String loginPassword, byte[] fileStream, int campaignId) throws java.rmi.RemoteException;

    /**
     * Upload contact file .<br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>loginPassword: Login Password of Spread.<br>fileType:
     * Type of upload data, include ".xls", ".csv", ".xlsx" <br>fileStream:
     * The upload file stream ( length<100MB ) <br>ContactListName: the name
     * of contact list .<br><br>return:  result
     */
    public java.lang.String uploadContactListFile(java.lang.String loginEmail, java.lang.String loginPassword, java.lang.String fileType, byte[] myFileStream, java.lang.String contactListName) throws java.rmi.RemoteException;

    /**
     * Validate Name exists<strong>(This method is only for reseller.)</strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>accountName:New account name<br>return: true if exists.Otherwise,false
     */
    public boolean nameExists(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String accountName) throws java.rmi.RemoteException;

    /**
     * Validate Email exists<strong>(This method is only for reseller.)</strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>accountEmail:New account name<br>return: true if exists.Otherwise,false
     */
    public boolean emailExists(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String accountEmail) throws java.rmi.RemoteException;

    /**
     * Validate Campaign exists<br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>APIKey: Login Password of Spread or APIKey API
     * Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>campaignName: Name of the campaign<br>return:campaign
     * ID
     */
    public int getCampaignID(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String campaignName) throws java.rmi.RemoteException;

    /**
     * Get access token<br>Arguments:<br><br>loginEmail: Login Email
     * of Spread.<br>APIKey: Login Password of Spread or APIKey API Key which
     * you can retrieve from your Spread account (My account=> Settings).<br>return:access
     * token
     */
    public java.lang.String getAccessToken(java.lang.String loginEmail, java.lang.String APIKey) throws java.rmi.RemoteException;

    /**
     * Verify access token<br>Arguments:<br><br>emailAddress:The emailAddress
     * of user<br>token: Access token<br>return true if succeed.
     */
    public boolean verifyAccessToken(java.lang.String emailAddress, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Get APIKey<br>Arguments:<br><br>loginEmail: Login Email of
     * Spread.<br>loginPassword: Login Password of Spread.<br>return:APIKey
     */
    public java.lang.String getAPIKey(java.lang.String loginEmail, java.lang.String loginPassword) throws java.rmi.RemoteException;

    /**
     * Get Account detail information<br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>loginPassword: Login Password of Spread.<br>return:table
     * column:EmailCredit,SentCount,MaxSubscriber,CurrentSubscribers,ExpiryDate,Status
     */
    public SpreadWS.javapackage.GetAccountInfoResponseGetAccountInfoResult getAccountInfo(java.lang.String loginEmail, java.lang.String loginPassword) throws java.rmi.RemoteException;

    /**
     * Setting Campaign DailyLimit<strong>(It is only for internal
     * use currently.)</strong><br>Arguments:<br><br>loginEmail: Login Email
     * of Spread.<br>APIKey: Login Password of Spread or APIKey API Key which
     * you can retrieve from your Spread account (My account=> Settings).<br>campaignID:The
     * Campaign ID which you want to set.<br>return true if success.
     */
    public boolean setCampaignDailyLimit(java.lang.String loginEmail, java.lang.String APIKey, int campaignid, int quantity) throws java.rmi.RemoteException;

    /**
     * Setting Campaign TimeLimit<strong>(It is only for internal
     * use currently.)</strong><br>Arguments:<br><br>loginEmail: Login Email
     * of Spread.<br>APIKey: Login Password of Spread or APIKey API Key which
     * you can retrieve from your Spread account (My account=> Settings).<br>campaignID:
     * The Campaign ID which you want to set.<br>TimeSpan: Send time limit
     * of campaign. format like: [ { '1': ['01:00-02:00','01:00-02:00'] },
     * { '2': ['01:00-02:00','01:00-02:00'] } ]<br>'0' means every day, '1':
     * Monday, '2': Tuesday,'3': Wednesday, '4': Thursday,'5': Friday, '6':
     * Saturday, '7': Sunday<br>return true if success.
     */
    public boolean setCampaignTimeLimit(java.lang.String loginEmail, java.lang.String APIKey, int campaignid, java.lang.String timeSpan) throws java.rmi.RemoteException;

    /**
     * SplitContactListByRange<strong>(It is only for internal use
     * currently.)</strong><br>Arguments:<br><br>loginEmail: Login Email
     * of Spread.<br>APIKey: Login Password of Spread or APIKey API Key which
     * you can retrieve from your Spread account (My account=> Settings).<br>SourceContactListName:
     * The Contact List which you want to split.<br>Range: The Range of Contact
     * List, format like: 0~10 <br>TargetContactListName: The Contact List
     * Which you want to insert.<br>return true if success.
     */
    public boolean splitContactListByRange(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String sourceContactListName, java.lang.String range, java.lang.String targetContactListName) throws java.rmi.RemoteException;

    /**
     * getActiveSubscribersByContactList(It is only for internal use
     * currently.)<strong></strong><br>Arguments:<br><br>loginEmail: Login
     * Email of Spread.<br>APIKey: Login Password of Spread or APIKey API
     * Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>contactListName: The Contact List you want to do.<br>return
     * the count of active subscribers.
     */
    public int getActiveSubscribersByContactList(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String contactListName) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public int createCampaignDraftSerialize(java.lang.String loginEmail, java.lang.String password, java.lang.String strcampaignArgs, java.lang.String[] category, int interval) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public int createCampaignSerialize(java.lang.String loginEmail, java.lang.String password, java.lang.String strcampaignArgs, java.lang.String[] category, int interval) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public int createCampaign2Serialize(java.lang.String loginEmail, java.lang.String password, java.lang.String campaignName, java.lang.String strcampaignCreatives, java.lang.String[] category, int interval, java.util.Calendar schedule, java.lang.String signature, SpreadWS.javapackage.CampaignStatus campaignStatus) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public java.lang.String getCampaignReportSerialize(java.lang.String loginEmail, java.lang.String password, int campaignID) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public boolean addSubscriberByInfoSerialize(java.lang.String loginEmail, java.lang.String password, java.lang.String strsubscriberArgs, java.lang.String subscription, SpreadWS.javapackage.DoubleOptIn optInType) throws java.rmi.RemoteException;

    /**
     * <b>(It is only for internal use currently.)</b>
     */
    public SpreadWS.javapackage.AddSubscribersByInfoSerializeResponseAddSubscribersByInfoSerializeResult addSubscribersByInfoSerialize(java.lang.String loginEmail, java.lang.String password, java.lang.String strsubscriberArgs, java.lang.String subscription, SpreadWS.javapackage.DoubleOptIn optInType) throws java.rmi.RemoteException;

    /**
     * getUserSentReport<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>campaignType: The type of campaign.<br>startDate:Starting
     * time<br>endDate:End time<br><br>return dataset.<br><br>Dataset Columns:<br>Year,Month,Sent
     */
    public SpreadWS.javapackage.GetUserSentReportResponseGetUserSentReportResult getUserSentReport(java.lang.String loginEmail, java.lang.String APIKey, SpreadWS.javapackage.AccountType accountType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException;

    /**
     * GetUserCredit<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>accountType: The type of account.<br>return dataset.<br><br>Dataset
     * Columns:<br>Credit,Sent,ExpiryDate
     */
    public SpreadWS.javapackage.GetUserCreditResponseGetUserCreditResult getUserCredit(java.lang.String loginEmail, java.lang.String APIKey, SpreadWS.javapackage.AccountType accountType) throws java.rmi.RemoteException;

    /**
     * GetSubscriberDetail<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>subscriber_email: The email of subscriber.<br>return
     * dataset.<br><br>Dataset Columns:<br>Credit,Sent,ExpiryDate
     */
    public SpreadWS.javapackage.GetSubscriberDetailResponseGetSubscriberDetailResult getSubscriberDetail(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String subscriber_email) throws java.rmi.RemoteException;

    /**
     * GetCampaignSubscriberDetail<strong></strong><br>Arguments:<br><br>loginEmail:
     * Login Email of Spread.<br>APIKey: Login Password of Spread or APIKey
     * API Key which you can retrieve from your Spread account (My account=>
     * Settings).<br>subscriber_email: The email of subscriber.<br>campaign_id:
     * The ID of campaign.<br>return dataset.<br><br>Dataset Columns:<br>Credit,Sent,ExpiryDate
     */
    public SpreadWS.javapackage.GetCampaignSubscriberDetailResponseGetCampaignSubscriberDetailResult getCampaignSubscriberDetail(java.lang.String loginEmail, java.lang.String APIKey, java.lang.String subscriber_email, int campaign_id) throws java.rmi.RemoteException;
}
