function eventDispatched_OrientAP(pId,pEvent)
{
	//alert("inside custom js event dispatch");
	if(pEvent.type=='change' )
	{		
	if(pId == 'po_year1')
	{
		
		return true;
	}
	if(pId == 'storehold_decsion')
	{
		return true;
	}
	if(pId == 'Text20')
	{
		return true;
	}
	if(pId == 'qualityhold_decision')
	{
		return true;
	}
	if(pId == 'Text40')
	{
		return true;
	}
	if(pId == 'Text35')
	{
		return true;
	}

		return true;
		}
		
	if(pEvent.type=='click' )
	{	
		if(pId == 'Button1')
		{
			alert("inside button1");
			return true;
		}
			if(pId == 'Button4')
		{
			return true;
		}
		if(pId == 'Button3')
		{
			return true;
		}
		if(pId == 'Button2')
		{
			return true;
		}
		if(pId == 'Button6')
		{
			return true;
		}
		
		if(pId == 'Button5')
		{
			return true;
		}
		return true;
		
	}
}

function validate_OrientAP(pEvent,activityName)
{
	var pr_number,stdec,qdec,storedec,qualitydec;
	var remarks_accounts,remarks_finance,remarks_gateentry,remarks_store,remarks_storehold,remarks_quality,remarks_qualityhold;
	var hiddenpark,hiddenpost;
	pr_number = document.getElementById('pr_number').value;
	stdec = document.getElementById('storehold_decsion').value;
	qdec = document.getElementById('qualityhold_decision').value;
	storedec = document.getElementById('store_decision').value;
	qualitydec = document.getElementById('quality_decision').value;
	remarks_finance = document.getElementById('remarks_finance').value;
	remarks_accounts = document.getElementById('remarks_accounts').value;
	remarks_gateentry = document.getElementById('remarks_gateentry').value;
	remarks_store = document.getElementById('remarks_store').value;
	remarks_storehold = document.getElementById('remarks_storehold').value;
	remarks_quality = document.getElementById('remarks_quality').value;
	remarks_qualityhold = document.getElementById('remarks_qualityhold').value;
	hiddenpark = document.getElementById('inv_park').value;
	hiddenpost = document.getElementById('inv_post').value;
	
	if(pEvent=='I' ||pEvent=='D')
	{	
	
		if((stdec == 'Purchase Return Required') && (pr_number == ''))
		{
			alert("Can't Leave Return PO Number field blank");
			return false;
		}
		if((qdec == 'Purchase Return Required') && (pr_number == ''))
		{
			alert("Can't Leave Return PO Number field blank");
			return false;
		}

		if(activityName == 'Store')
		{
			
			if(storedec == '')
			{
				alert("Please take decision for Store");
				return false;
			}
			if(remarks_store == '')
			{
				alert("Please enter some Remarks for Store");
				return false;
			}
		}
		if(activityName == 'Store Hold')
		{
			if(stdec == '')
			{
				alert("Please take decision for Store Hold");
				return false;
			}
			if(remarks_storehold == '')
			{
				alert("Please enter some Remarks for Store Hold");
				return false;
			}
		}
		if(activityName == 'Quality')
		{
			if(qualitydec == '')
			{
				alert("Please take decision for Quality");
				return false;
			}
			if(remarks_quality == '')
			{
				alert("Please enter some Remarks for Quality");
				return false;
			}
		}
		if(activityName == 'Quality_Hold')
		{
			if(qdec == '')
			{
				alert("Please take decision for Quality Hold");
				return false;
			}
			if(remarks_qualityhold == '')
			{
				alert("Please enter some Remarks for Quality Hold");
				return false;
			}
		}
		if(activityName == 'Accounts')
		{
			if(remarks_accounts == '')
			{
				alert("Please enter some Remarks for Accounts");
				return false;
			}
			if(hiddenpark == '')
			{
				alert("Please do the Parking for the current Purchase Order and Invoice");
				return false;
			}
		
		}
		if(activityName == 'Finance')
		{
			if(remarks_finance == '')
			{
				alert("Please enter some Remarks for Finance");
				return false;
			}
				if(hiddenpost == '')
			{
				alert("Please do the Posting for the current Purchase Order and Invoice");
				return false;
			}
		}
		if(activityName == 'Gate Entry')
		{
			if(remarks_gateentry == '')
			{
				alert("Please enter some Remarks for Gate Entry");
				return false;
			}
		}
			return true;
		 
	}
	
	if(pEvent=='S')
	{
			return true;
		
   }
	
	return true;
	
}

