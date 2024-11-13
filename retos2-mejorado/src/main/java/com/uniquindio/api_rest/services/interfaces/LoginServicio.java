package com.uniquindio.api_rest.services.interfaces;

import com.uniquindio.api_rest.dto.*;

public interface LoginServicio {
    TokenDTO login(LoginDTO loginDTO);
}
