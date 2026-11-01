import React, { useState, useEffect } from 'react';
import { useApp } from '../../context/AppContext';
import { CircleHelp, Lock, AlertCircle, CheckCircle2, Loader2, ShieldCheck, User, Building, Mail, Phone, KeyRound, FileText, Upload } from 'lucide-react';
import { Modal } from '../common/Modal';
import { HeroVideo } from '../common/HeroVideo';
import './LoginPage.css';
import { BACKEND_URL, checkBackendReady } from '../../services/backendConnection';
import { getPasswordError } from '../../utils/passwordValidation';

export function LoginPage() {
  const { showToast, setCurrentUser, setActivePage, login } = useApp();
  const [authTab, setAuthTab] = useState('login'); // 'login' | 'register'
  const [infoModal, setInfoModal] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  // Form states for Login
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');

  // Form states for Registration
  const [regRole, setRegRole] = useState('CANDIDATE'); // 'CANDIDATE' | 'EMPLOYER'
  const [regFullName, setRegFullName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regPhone, setRegPhone] = useState('');
  const [regCompanyName, setRegCompanyName] = useState('');
  const [regLicenseFile, setRegLicenseFile] = useState(null);
  const [regPassword, setRegPassword] = useState('');
  const [regConfirmPassword, setRegConfirmPassword] = useState('');

  useEffect(() => {
    if (typeof window !== 'undefined') {
      try {
        const params = new URLSearchParams(window.location.search);
        const oauthErr = params.get('oauthError');
        if (oauthErr) {
          if (oauthErr.includes('authorization_request_not_found')) {
            setErrorMessage('Phiên đăng nhập Google đã hết hạn. Vui lòng bấm nút "Đăng nhập bằng Google" bên dưới để thực hiện lại.');
          } else {
            try {
              setErrorMessage(decodeURIComponent(oauthErr.replace(/\+/g, ' ')));
            } catch {
              setErrorMessage(oauthErr);
            }
          }
        }
        if (params.get('error') === 'locked') {
          setErrorMessage('Tài khoản của bạn đã bị khóa bởi quản trị viên. Vui lòng liên hệ hỗ trợ.');
          showToast('Tài khoản đã bị khóa', 'danger');
          window.history.replaceState({}, document.title, window.location.pathname);
        }
      } catch (err) {
        console.error("URL search params error:", err);
      }
    }
  }, [showToast]);

  // Handle Manual Form Login
  const handleManualLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    const cleanIdentifier = loginEmail.trim();
    if (!cleanIdentifier || !loginPassword.trim()) {
      setIsLoading(false);
      setErrorMessage('Vui lòng nhập đầy đủ email/tên đăng nhập và mật khẩu.');
      return;
    }

    try {
      const res = await fetch(`${BACKEND_URL}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: cleanIdentifier, password: loginPassword }),
      });

      const result = await res.json();
      if (!res.ok) {
        throw new Error(result.message || 'Email hoặc mật khẩu không chính xác.');
      }

      const user = result.data;
      if (user.token) {
        localStorage.setItem('careerfit_token', user.token);
      }
      localStorage.setItem('careerfit_current_user', JSON.stringify(user));
      setCurrentUser(user);
      showToast('Đăng nhập thành công!', 'success');

      const targetPage = user.role === 'EMPLOYER' 
        ? 'employer-dashboard' 
        : (user.role === 'ADMIN' ? 'admin-dashboard' : 'candidate-dashboard');
      setActivePage(targetPage);
    } catch (err) {
      // Fallback to local context logic if backend connection fails
      const fallback = login ? login(cleanIdentifier, loginPassword) : null;
      if (fallback && fallback.success) {
        setErrorMessage('');
        showToast('Đăng nhập thành công!', 'success');
        const user = fallback.user;
        const targetPage = user?.role === 'EMPLOYER' 
          ? 'employer-dashboard' 
          : (user?.role === 'ADMIN' ? 'admin-dashboard' : 'candidate-dashboard');
        setActivePage(targetPage);
      } else {
        const errorText = (fallback && fallback.message) || err.message || 'Đăng nhập không thành công.';
        setErrorMessage(errorText);
        showToast(errorText, 'danger');
      }
    } finally {
      setIsLoading(false);
    }
  };

  // Handle Manual Form Register
  const handleManualRegister = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    if (!regFullName.trim() || !regEmail.trim() || !regPassword || !regPhone.trim()) {
      setIsLoading(false);
      setErrorMessage('Vui lòng điền đầy đủ các thông tin bắt buộc.');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(regEmail.trim())) {
      setIsLoading(false);
      setErrorMessage('Email không đúng định dạng.');
      return;
    }

    const passErr = getPasswordError(regPassword);
    if (passErr) {
      setIsLoading(false);
      setErrorMessage(passErr);
      return;
    }

    if (regPassword !== regConfirmPassword) {
      setIsLoading(false);
      setErrorMessage('Mật khẩu xác nhận không khớp.');
      return;
    }

    const phoneDigits = regPhone.replace(/\s+/g, '');
    const phoneRegex = /^(03|05|07|08|09)\d{8}$/;
    if (!phoneRegex.test(phoneDigits)) {
      setIsLoading(false);
      setErrorMessage('Số điện thoại không hợp lệ. Phải có 10 số, bắt đầu bằng 03/05/07/08/09.');
      return;
    }

    if (regRole === 'EMPLOYER') {
      if (!regCompanyName.trim()) {
        setIsLoading(false);
        setErrorMessage('Vui lòng nhập tên công ty.');
        return;
      }
      if (!regLicenseFile) {
        setIsLoading(false);
        setErrorMessage('Vui lòng tải lên Giấy phép kinh doanh (PDF, PNG hoặc JPG).');
        return;
      }
    }

    try {
      if (regRole === 'CANDIDATE') {
        const res = await fetch(`${BACKEND_URL}/api/auth/register/candidate`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            fullName: regFullName.trim(),
            email: regEmail.trim().toLowerCase(),
            password: regPassword,
            confirmPassword: regConfirmPassword,
            phone: phoneDigits,
            targetRole: 'Fresher Developer',
            location: 'Hà Nội',
            currentLevel: 'FRESHER'
          })
        });

        const data = await res.json();
        if (!res.ok) {
          throw new Error(data.message || 'Đăng ký không thành công.');
        }

        setSuccessMessage('Đăng ký tài khoản Ứng viên thành công! Vui lòng đăng nhập.');
        showToast('Đăng ký thành công!', 'success');
        setAuthTab('login');
        setLoginEmail(regEmail.trim());
        setLoginPassword('');
      } else {
        // Employer registration
        const formData = new FormData();
        formData.append('recruiterName', regFullName.trim());
        formData.append('companyName', regCompanyName.trim());
        formData.append('email', regEmail.trim().toLowerCase());
        formData.append('password', regPassword);
        formData.append('confirmPassword', regConfirmPassword);
        formData.append('phone', phoneDigits);
        formData.append('location', 'Hà Nội');
        formData.append('licenseFile', regLicenseFile);

        const res = await fetch(`${BACKEND_URL}/api/auth/register/employer`, {
          method: 'POST',
          body: formData
        });

        const data = await res.json();
        if (!res.ok) {
          throw new Error(data.message || 'Đăng ký nhà tuyển dụng không thành công.');
        }

        setSuccessMessage('Đăng ký tài khoản Nhà tuyển dụng thành công! Đang chờ kiểm duyệt.');
        showToast('Đăng ký thành công!', 'success');
        setAuthTab('login');
        setLoginEmail(regEmail.trim());
        setLoginPassword('');
      }
    } catch (err) {
      setErrorMessage(err.message || 'Đăng ký thất bại. Vui lòng thử lại.');
      showToast(err.message || 'Đăng ký thất bại.', 'danger');
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleOAuthLogin = async () => {
    setIsLoading(true);
    setErrorMessage('');
    const isReady = await checkBackendReady();
    if (!isReady) {
      setIsLoading(false);
      const msg = 'Không thể kết nối máy chủ đăng nhập. Hãy khởi động backend rồi thử lại.';
      setErrorMessage(msg);
      showToast(msg, 'warning');
      return;
    }
    window.location.href = `${BACKEND_URL}/oauth2/authorization/google`;
  };

  const handleMicrosoftOAuthLogin = async () => {
    setIsLoading(true);
    setErrorMessage('');
    const isReady = await checkBackendReady();
    if (!isReady) {
      setIsLoading(false);
      const msg = 'Không thể kết nối máy chủ đăng nhập. Hãy khởi động backend rồi thử lại.';
      setErrorMessage(msg);
      showToast(msg, 'warning');
      return;
    }
    window.location.href = `${BACKEND_URL}/oauth2/authorization/microsoft`;
  };

  return (
    <div className="login-page">
      <HeroVideo className="login-video" />
      <section className="login-content" aria-labelledby="login-heading">
        <div className="login-main-content">
          <div className="login-lock animate-fade-rise" aria-hidden="true"><Lock /></div>
          <h1 id="login-heading" className="login-heading animate-fade-rise">
            <span>Chào mừng</span>
            <span className="login-heading-accent">
              {authTab === 'login' ? 'quay trở lại.' : 'đến CareerFit.'}
            </span>
          </h1>
          <p className="login-subtitle animate-fade-rise-delay">
            {authTab === 'login' 
              ? 'Một bước gần hơn đến cơ hội dành cho bạn. Đăng nhập để tiếp tục.' 
              : 'Tạo tài khoản mới để bắt đầu khám phá cơ hội việc làm IT hàng đầu.'}
          </p>

          <div className="animate-fade-rise-delay-2">
            <div className="login-card" aria-busy={isLoading}>
              
              {/* Tab Switcher */}
              <div className="auth-tab-group" role="tablist">
                <button
                  type="button"
                  id="tab-login"
                  data-testid="tab-login"
                  role="tab"
                  aria-selected={authTab === 'login'}
                  className={`auth-tab-btn ${authTab === 'login' ? 'active' : ''}`}
                  onClick={() => { setAuthTab('login'); setErrorMessage(''); setSuccessMessage(''); }}
                >
                  Đăng nhập
                </button>
                <button
                  type="button"
                  id="tab-register"
                  data-testid="tab-register"
                  role="tab"
                  aria-selected={authTab === 'register'}
                  className={`auth-tab-btn ${authTab === 'register' ? 'active' : ''}`}
                  onClick={() => { setAuthTab('register'); setErrorMessage(''); setSuccessMessage(''); }}
                >
                  Đăng ký
                </button>
              </div>

              {/* Status Alert Messages */}
              {errorMessage && (
                <div id="auth-error-message" data-testid="auth-error" role="alert" className="login-error">
                  <AlertCircle size={18} aria-hidden="true" />
                  <span>{errorMessage}</span>
                </div>
              )}

              {successMessage && (
                <div id="auth-success-message" data-testid="auth-success" role="status" className="login-success">
                  <CheckCircle2 size={18} aria-hidden="true" />
                  <span>{successMessage}</span>
                </div>
              )}

              {/* TAB 1: MANUAL LOGIN FORM */}
              {authTab === 'login' ? (
                <form id="form-login" data-testid="form-login" onSubmit={handleManualLogin} className="auth-form">
                  <div className="auth-field">
                    <label htmlFor="login-email">Email hoặc Tên đăng nhập *</label>
                    <div className="auth-input-wrapper">
                      <Mail size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="login-email"
                        name="email"
                        data-testid="login-email"
                        type="text"
                        required
                        placeholder="candidate@gmail.com"
                        value={loginEmail}
                        onChange={(e) => setLoginEmail(e.target.value)}
                        autoComplete="username"
                      />
                    </div>
                  </div>

                  <div className="auth-field">
                    <label htmlFor="login-password">Mật khẩu *</label>
                    <div className="auth-input-wrapper">
                      <KeyRound size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="login-password"
                        name="password"
                        data-testid="login-password"
                        type="password"
                        required
                        placeholder="••••••••"
                        value={loginPassword}
                        onChange={(e) => setLoginPassword(e.target.value)}
                        autoComplete="current-password"
                      />
                    </div>
                  </div>

                  <button
                    type="submit"
                    id="login-submit-btn"
                    data-testid="login-submit"
                    disabled={isLoading}
                    className="auth-submit-btn"
                  >
                    {isLoading ? <Loader2 className="animate-spin" size={16} /> : 'Đăng nhập'}
                  </button>
                </form>
              ) : (
                /* TAB 2: MANUAL REGISTER FORM */
                <form id="form-register" data-testid="form-register" onSubmit={handleManualRegister} className="auth-form">
                  <div className="auth-role-picker" role="radiogroup" aria-label="Chọn vai trò">
                    <button
                      type="button"
                      id="role-candidate-btn"
                      data-testid="role-candidate"
                      role="radio"
                      aria-checked={regRole === 'CANDIDATE'}
                      className={`auth-role-option ${regRole === 'CANDIDATE' ? 'active' : ''}`}
                      onClick={() => setRegRole('CANDIDATE')}
                    >
                      <User size={15} /> Ứng viên
                    </button>
                    <button
                      type="button"
                      id="role-employer-btn"
                      data-testid="role-employer"
                      role="radio"
                      aria-checked={regRole === 'EMPLOYER'}
                      className={`auth-role-option ${regRole === 'EMPLOYER' ? 'active' : ''}`}
                      onClick={() => setRegRole('EMPLOYER')}
                    >
                      <Building size={15} /> Doanh nghiệp
                    </button>
                  </div>

                  <div className="auth-field">
                    <label htmlFor="register-fullname">
                      {regRole === 'CANDIDATE' ? 'Họ và tên *' : 'Tên người đại diện *'}
                    </label>
                    <div className="auth-input-wrapper">
                      <User size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="register-fullname"
                        name="fullName"
                        data-testid="register-fullname"
                        type="text"
                        required
                        placeholder="Nguyễn Văn A"
                        value={regFullName}
                        onChange={(e) => setRegFullName(e.target.value)}
                      />
                    </div>
                  </div>

                  {regRole === 'EMPLOYER' && (
                    <>
                      <div className="auth-field">
                        <label htmlFor="register-company">Tên công ty *</label>
                        <div className="auth-input-wrapper">
                          <Building size={16} className="auth-input-icon" aria-hidden="true" />
                          <input
                            id="register-company"
                            name="companyName"
                            data-testid="register-company"
                            type="text"
                            required
                            placeholder="FPT Software, Viettel..."
                            value={regCompanyName}
                            onChange={(e) => setRegCompanyName(e.target.value)}
                          />
                        </div>
                      </div>

                      <div className="auth-field">
                        <label htmlFor="register-license">Giấy phép kinh doanh (PDF, PNG, JPG) *</label>
                        <div className="auth-file-wrapper">
                          <input
                            id="register-license"
                            name="licenseFile"
                            data-testid="register-license"
                            type="file"
                            accept=".pdf,.png,.jpg,.jpeg"
                            onChange={(e) => {
                              const file = e.target.files[0];
                              if (file) {
                                if (!/\.(pdf|png|jpe?g)$/i.test(file.name)) {
                                  setErrorMessage('Vui lòng chọn tệp định dạng PDF, PNG hoặc JPG.');
                                  setRegLicenseFile(null);
                                  return;
                                }
                                if (file.size > 10 * 1024 * 1024) {
                                  setErrorMessage('Dung lượng tệp không được vượt quá 10 MB.');
                                  setRegLicenseFile(null);
                                  return;
                                }
                                setErrorMessage('');
                                setRegLicenseFile(file);
                              }
                            }}
                          />
                          {regLicenseFile && (
                            <span className="auth-file-selected" data-testid="selected-license-name">
                              <FileText size={14} /> {regLicenseFile.name} ({(regLicenseFile.size / 1024).toFixed(1)} KB)
                            </span>
                          )}
                        </div>
                      </div>
                    </>
                  )}

                  <div className="auth-field">
                    <label htmlFor="register-email">Email {regRole === 'EMPLOYER' ? 'công việc *' : '*'}</label>
                    <div className="auth-input-wrapper">
                      <Mail size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="register-email"
                        name="email"
                        data-testid="register-email"
                        type="email"
                        required
                        placeholder="name@example.com"
                        value={regEmail}
                        onChange={(e) => setRegEmail(e.target.value)}
                      />
                    </div>
                  </div>

                  <div className="auth-field">
                    <label htmlFor="register-phone">Số điện thoại *</label>
                    <div className="auth-input-wrapper">
                      <Phone size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="register-phone"
                        name="phone"
                        data-testid="register-phone"
                        type="tel"
                        required
                        placeholder="0912345678"
                        value={regPhone}
                        onChange={(e) => setRegPhone(e.target.value)}
                      />
                    </div>
                  </div>

                  <div className="auth-field">
                    <label htmlFor="register-password">Mật khẩu *</label>
                    <div className="auth-input-wrapper">
                      <KeyRound size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="register-password"
                        name="password"
                        data-testid="register-password"
                        type="password"
                        required
                        placeholder="Tối thiểu 8 ký tự, 1 hoa, 1 số, 1 ký tự đặc biệt"
                        value={regPassword}
                        onChange={(e) => setRegPassword(e.target.value)}
                      />
                    </div>
                  </div>

                  <div className="auth-field">
                    <label htmlFor="register-confirm-password">Xác nhận mật khẩu *</label>
                    <div className="auth-input-wrapper">
                      <KeyRound size={16} className="auth-input-icon" aria-hidden="true" />
                      <input
                        id="register-confirm-password"
                        name="confirmPassword"
                        data-testid="register-confirm-password"
                        type="password"
                        required
                        placeholder="Nhập lại mật khẩu"
                        value={regConfirmPassword}
                        onChange={(e) => setRegConfirmPassword(e.target.value)}
                      />
                    </div>
                  </div>

                  <button
                    type="submit"
                    id="register-submit-btn"
                    data-testid="register-submit"
                    disabled={isLoading}
                    className="auth-submit-btn"
                  >
                    {isLoading ? <Loader2 className="animate-spin" size={16} /> : 'Đăng ký tài khoản'}
                  </button>
                </form>
              )}

              {/* DIVIDER */}
              <div className="auth-divider">
                <span>Hoặc tiếp tục bằng</span>
              </div>

              {/* OAUTH PROVIDERS */}
              <div className="login-providers">
                <button
                  type="button"
                  id="oauth-google-btn"
                  data-testid="oauth-google"
                  onClick={handleGoogleOAuthLogin}
                  disabled={isLoading}
                  className="login-provider login-provider--google"
                >
                  <svg aria-hidden="true" className="login-provider-icon" viewBox="0 0 24 24">
                    <path fill="#4285F4" d="M23.745 12.27c0-.7-.06-1.4-.19-2.07H12v4.51h6.6c-.29 1.52-1.14 2.82-2.4 3.68v3.05h3.88c2.27-2.09 3.665-5.17 3.665-9.17z" />
                    <path fill="#34A853" d="M12 24c3.24 0 5.95-1.08 7.93-2.91l-3.88-3.05c-1.08.72-2.45 1.16-4.05 1.16-3.12 0-5.77-2.11-6.72-4.96H1.29v3.15C3.26 21.3 7.31 24 12 24z" />
                    <path fill="#FBBC05" d="M5.28 14.24c-.25-.72-.38-1.49-.38-2.24s.13-1.52.38-2.24V6.61H1.29C.47 8.24 0 10.06 0 12s.47 3.76 1.29 5.39l3.99-3.15z" />
                    <path fill="#EA4335" d="M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.95 1.19 15.24 0 12 0 12 0 7.31 0 3.26 2.7 1.29 6.61l3.99 3.15c.95-2.85 3.6-4.96 6.72-4.96z" />
                  </svg>
                  <span>Tiếp tục đăng nhập bằng Google</span>
                </button>
    
                <button
                  type="button"
                  id="oauth-microsoft-btn"
                  data-testid="oauth-microsoft"
                  onClick={handleMicrosoftOAuthLogin}
                  disabled={isLoading}
                  className="login-provider login-provider--microsoft"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 21 21" aria-hidden="true" className="login-provider-icon">
                    <rect x="1" y="1" width="9" height="9" fill="#f25022"/>
                    <rect x="11" y="1" width="9" height="9" fill="#7fba00"/>
                    <rect x="1" y="11" width="9" height="9" fill="#00a4ef"/>
                    <rect x="11" y="11" width="9" height="9" fill="#ffb900"/>
                  </svg>
                  <span>Tiếp tục đăng nhập bằng Microsoft (Outlook)</span>
                </button>
              </div>

              {isLoading && <p className="login-progress" role="status"><Loader2 className="animate-spin" size={18} /> Đang xử lý...</p>}
            </div>
          </div>

          <div className="animate-fade-rise-delay-2">
            <nav className="login-secondary" aria-label="Hỗ trợ đăng nhập">
              <button type="button" onClick={() => setInfoModal('help')}>
                <CircleHelp aria-hidden="true" />
                <span>Trợ giúp & Hướng dẫn</span>
              </button>
            </nav>
            <p className="login-legal">Bằng cách tiếp tục, bạn đồng ý với <button type="button" onClick={() => setInfoModal('terms')}>Điều khoản dịch vụ</button> và <button type="button" onClick={() => setInfoModal('privacy')}>Chính sách bảo mật</button> của CareerFit.</p>
          </div>
        </div>

        <div className="login-aside animate-fade-rise-delay-2">
          <p className="login-trust"><ShieldCheck aria-hidden="true" /><span>Kết nối an toàn.<br /><span>Dành cho sinh viên, giảng viên và doanh nghiệp.</span></span></p>
        </div>
      </section>
      <footer className="login-footer">© {new Date().getFullYear()} CareerFit. Nền tảng tuyển dụng và phát triển nghề nghiệp công nghệ.</footer>

      {/* Info / Help Modal */}
      <Modal isOpen={Boolean(infoModal)} onClose={() => setInfoModal(null)} title={infoModal === 'help' ? 'Trợ giúp & Hướng dẫn' : infoModal === 'terms' ? 'Điều khoản dịch vụ' : 'Chính sách bảo mật'}>
        <div className="text-sm text-slate-600 leading-relaxed space-y-3">
          {infoModal === 'help' ? (
            <>
              <p>Bạn có thể đăng nhập bằng tài khoản email/mật khẩu trực tiếp hoặc thông qua Google/Microsoft.</p>
              <p>Tài khoản mẫu để kiểm thử: <code>candidate@gmail.com</code> hoặc <code>employer@gmail.com</code> (Mật khẩu: <code>Candidate@123</code> hoặc <code>123456</code>).</p>
            </>
          ) : (
            <p>{infoModal === 'terms' ? 'Nội dung điều khoản dịch vụ của CareerFit.' : 'Nội dung chính sách bảo mật của CareerFit.'}</p>
          )}
        </div>
      </Modal>
    </div>
  );
}
